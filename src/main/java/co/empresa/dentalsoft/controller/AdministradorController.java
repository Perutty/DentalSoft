package co.empresa.dentalsoft.controller;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.Cita;
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.Tratamiento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EpsService;
import co.empresa.dentalsoft.service.EstadoCivilService;
import co.empresa.dentalsoft.service.EvolucionService;
import co.empresa.dentalsoft.service.HistoriaClinicaService;
import co.empresa.dentalsoft.service.HoraService;
import co.empresa.dentalsoft.service.OdontologoService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.service.PaisService;
import co.empresa.dentalsoft.service.SexoService;
import co.empresa.dentalsoft.service.TipoDocumentoService;
import co.empresa.dentalsoft.service.TratamientoService;
import co.empresa.dentalsoft.service.impl.CloudinaryService;
import co.empresa.dentalsoft.service.impl.EmailService;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

	@Autowired
	private AdministradorService administradorService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private OdontologoService odontologoService;

	@Autowired
	private TipoDocumentoService tipoDocumentoService;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private PaisService paisService;

	@Autowired
	private CitaService citaService;

	@Autowired
	private EpsService epsService;

	@Autowired
	private TratamientoService tratamientoService;

	@Autowired
	private HoraService horaService;

	@Autowired
	private HistoriaClinicaService historiaClinicaService;

	@Autowired
	private EstadoCivilService estadoCivilService;

	@Autowired
	private EvolucionService evolucionService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private CloudinaryService cloudinaryService;

	List<Cita> listCitasByPaciente = new ArrayList<>();
	HistoriaClinica historia = new HistoriaClinica();
	List<Cita> listBuscarCita = new ArrayList<>();

	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {

		if (request.getSession().getAttribute("admin_doc") != null) {
			return "redirect:/admin/admindashboard";
		} else
			return "loginadmin";
	}

	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password,
			HttpServletRequest request, Model model) {

		Administrador admin = administradorService.select(documento, password);
		if (admin != null) {
			request.getSession().setAttribute("admin_doc", documento);
			return "redirect:/admin/dashboard";
		} else {
			att.addFlashAttribute("loginError", "Documento o contraseña incorrecta");
			return "redirect:/admin/login";
		}
	}

	@GetMapping("/enviarcorreo/{documento}/{fecha}/{hora}/{doctor}")
	public String enviarCorreo(RedirectAttributes att, HttpServletRequest request,
			@PathVariable("documento") String documento, @PathVariable("fecha") String fecha,
			@PathVariable("hora") String hora, @PathVariable("doctor") String doctor, Model model)
			throws ParseException {
		Paciente paciente = pacienteService.get(documento);
		emailService.sendEmail("" + paciente.getCorreo(), "Recordatorio cita odontológica", "Señor: "
				+ paciente.getNombre() + "\n\nCordial saludo\n\n\nLe recordamos que tiene una cita programada el día "
				+ fecha + " a las " + hora + ", con el " + doctor
				+ " en el edificio Colegio Médico oficina 402.\n\n\nPor favor, si no puede asistir, notifíquenos por nuestros medios de atención.\n\n\n\nGracias por su atención.");

		att.addFlashAttribute("accion", "¡Notificación de cita enviada con éxito!");

		return "redirect:/admin/citas/" + documento;
	}

	@GetMapping("/edit/{documento}")
	public String editForAdmin(HttpServletRequest request, @PathVariable("documento") String documento, Model model) {
		String adm_doc = (String) request.getSession().getAttribute("admin_doc");

		model.addAttribute("tipoDoc", tipoDocumentoService.getAll());
		model.addAttribute("estadocivil", estadoCivilService.getAll());
		model.addAttribute("eps", epsService.getAll());
		model.addAttribute("sexo", sexoService.getAll());
		model.addAttribute("pais", paisService.getAll());
		model.addAttribute("paci", pacienteService.get(documento));
		model.addAttribute("adm", administradorService.get(adm_doc));
		model.addAttribute("isAdmin", true);
		return "editpaciente";
	}

	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model) {

		List<Paciente> pacientes = pacienteService.getAll();
		pacientes.sort(Comparator.comparing(Paciente::getNombre));
		
		model.addAttribute("tipoDoc", tipoDocumentoService.getAll());
		model.addAttribute("estadocivil", estadoCivilService.getAll());
		model.addAttribute("paciente", pacientes);
		model.addAttribute("eps", epsService.getAll());
		model.addAttribute("sexo", sexoService.getAll());
		model.addAttribute("pais", paisService.getAll());
		model.addAttribute("admin", administradorService.get((String) request.getSession().getAttribute("admin_doc")));
		model.addAttribute("isAdmin", true);

		return "admindashboard";
	}

	@GetMapping("/citas/{documento}")
	public String citas(HttpServletRequest request, @PathVariable("documento") String documento, Model model) {

		request.getSession().setAttribute("docPaci", documento);
		List<HistoriaClinica> listHC = historiaClinicaService.getAll();
		List<Cita> listCitas = citaService.getAll();
		List<Tratamiento> tratamientos = tratamientoService.getAll();
		listCitasByPaciente.clear();
		listCitas.forEach((cita) -> {
			if (cita.getPaciente_doc().equals(pacienteService.get(documento).getNombre()))
				listCitasByPaciente.add(cita);
		});
		listHC.forEach((hc) -> {
			if (hc.getPaciente_doc().equals(pacienteService.get(documento).getDocumento()))
				model.addAttribute("historia", hc.getId());
				
		});
		listCitasByPaciente.sort(Comparator.comparing(Cita::getFecha).reversed().thenComparing(Cita::getHora).reversed());
		tratamientos.sort(Comparator.comparing(Tratamiento::getNombre));
		model.addAttribute("tratamientos", tratamientos);
		model.addAttribute("horas", horaService.getAll());
		model.addAttribute("evos", evolucionService.getAll());
		model.addAttribute("odontologos", odontologoService.getAll());
		model.addAttribute("nombre", pacienteService.get(documento).getNombre());
		model.addAttribute("documento", documento);
		
		model.addAttribute("paci", pacienteService.get(documento));
		model.addAttribute("citas", listCitasByPaciente);
		model.addAttribute("admin", administradorService.get((String) request.getSession().getAttribute("admin_doc")));
		return "citaspaciente";
	}

	@GetMapping("/edit")
	public String edit(HttpServletRequest request, Model model) {
		model.addAttribute("tipoDoc", tipoDocumentoService.getAll());
		model.addAttribute("admin", administradorService.get((String)request.getSession().getAttribute("admin_doc")));
		model.addAttribute("isAdmin",true);
		return "editpaciente";
	}

	@PostMapping("/save")
	public String save(RedirectAttributes att, Administrador administrador, Model model) {
		if (administrador != null) {
			administradorService.save(administrador);
			att.addFlashAttribute("accion", "¡Datos actualizados con éxito!");
		}
		return "redirect:/admin/dashboard";
	}

	@GetMapping("/delete/{id}")
	public String delete(RedirectAttributes att, @PathVariable("id") String id, Model model) {
		citaService.delete(Integer.parseInt(id));
		att.addFlashAttribute("accion", "¡Cita eliminada con éxito!");
		return "redirect:/cita/list";
	}

	@PostMapping("/editFotoAdmin")
	public String editFotoAdmin(RedirectAttributes att, @RequestParam MultipartFile file,
			HttpServletRequest request, Model model) throws Exception {
		
		cloudinaryService.getImage(administradorService.get((String) request.getSession().getAttribute("admin_doc")).getFoto());
		administradorService.get((String) request.getSession().getAttribute("admin_doc")).setFoto(cloudinaryService.upload(file).get("url").toString());
		
		att.addFlashAttribute("accion", "¡Foto del perfil actualizada con éxito!");

		return "redirect:/admin/edit";
	}

	@PostMapping("/editDatosAdmin")
	public String editDatosAdmin(RedirectAttributes att, @RequestParam("documento") String documento,
			@RequestParam("nombre") String nombre, @RequestParam("tipodoc") String tipodoc,
			@RequestParam("correo") String correo, @RequestParam("celular") String celular,
			@RequestParam("password") String password, Model model) {
		Administrador adm = administradorService.get(documento);
		adm.setNombre(nombre);
		adm.setTipodoc(tipodoc);
		adm.setCelular(celular);
		adm.setCorreo(correo);
		adm.setDocumento(documento);
		adm.setPassword(password);
		adm.setFoto(adm.getFoto());
		att.addFlashAttribute("accion", "¡Datos personales actualizados con éxito!");
		return "redirect:/admin/edit";
	}

	@PostMapping("/editDatosPaciente")
	public String editPaciente(RedirectAttributes att, Paciente paciente, HttpServletRequest request, Model model) {
		List<Cita> citas = citaService.getAll();
		citas.forEach((cita) -> {
			if (cita.getPaciente_doc().equals(pacienteService.get(paciente.getDocumento()).getNombre())) {
				cita.setPaciente_doc(pacienteService.get(paciente.getDocumento()).getNombre());
				citaService.save(cita);
			}
		});
		Paciente paci = pacienteService.get(paciente.getDocumento());
		paci.setTipodoc(paciente.getTipodoc());
		paci.setNombre(paciente.getNombre());
		paci.setSexo(paciente.getSexo());
		paci.setEstadocivil(paciente.getEstadocivil());
		paci.setPaisnac(paciente.getPaisnac());
		paci.setCiudadnac(paciente.getCiudadnac());
		paci.setFechanac(paciente.getFechanac());
		paci.setFechaingreso(paciente.getFechaingreso());
		paci.setPaisdomi(paciente.getPaisdomi());
		paci.setCiudaddomi(paciente.getPaisdomi());
		paci.setDirecciondomi(paciente.getDirecciondomi());
		paci.setBarriodomi(paciente.getBarriodomi());
		paci.setCelular(paciente.getCelular());
		paci.setCorreo(paciente.getCorreo());
		paci.setOcupacion(paciente.getOcupacion());
		paci.setEps(paciente.getEps());
		paci.setTipoafiliacion(paciente.getTipoafiliacion());
		paci.setPoliza(paciente.getPoliza());
		paci.setFoto(pacienteService.get(paciente.getDocumento()).getFoto());
		att.addFlashAttribute("accion", "¡Datos del paciente actualizados con éxito!");
		return "redirect:/admin/edit/" + pacienteService.get(paciente.getDocumento()).getDocumento();
	}

	@PostMapping("/editDatosOdontologo")
	public String editOdontologo(RedirectAttributes att, Odontologo odontologo, HttpServletRequest request,
			Model model) {
		odontologoService.get(odontologo.getDocumento()).setTipodoc(odontologo.getTipodoc());
		odontologoService.get(odontologo.getDocumento()).setNombre(odontologo.getNombre());
		odontologoService.get(odontologo.getDocumento()).setCelular(odontologo.getCelular());
		odontologoService.get(odontologo.getDocumento()).setCorreo(odontologo.getCorreo());
		odontologoService.get(odontologo.getDocumento()).setFoto(odontologoService.get(odontologo.getDocumento()).getFoto());
		att.addFlashAttribute("accion", "¡Datos del odontólogo actualizados con éxito!");
		return "redirect:/odontologo/edit/" + odontologo.getDocumento();
	}

	@PostMapping("/editFotoPaciente")
	public String editFotoPaciente(RedirectAttributes att, @RequestParam MultipartFile file,
			@RequestParam("documento") String documento, HttpServletRequest request, Model model) throws Exception {
		cloudinaryService.getImage(pacienteService.get(documento).getFoto());
		pacienteService.get(documento).setFoto(cloudinaryService.upload(file).get("url").toString());
		att.addFlashAttribute("accion", "¡Foto del perfil actualizada con éxito!");
		return "redirect:/admin/edit/" + pacienteService.get(documento).getDocumento();
	}

	@PostMapping("/editFotoOdontologo")
	public String editFotoOdontologo(RedirectAttributes att, @RequestParam MultipartFile file,
			@RequestParam("documento") String documento, HttpServletRequest request, Model model) throws Exception {

		cloudinaryService.getImage(odontologoService.get(documento).getFoto());
		odontologoService.get(documento).setFoto(cloudinaryService.upload(file).get("url").toString());
		att.addFlashAttribute("accion", "¡Foto del perfil actualizada con éxito!");
		return "redirect:/odontologo/edit/" + odontologoService.get(documento).getDocumento();
	}

	@GetMapping("/buscar")
	public String buscarCitas(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
			@RequestParam("estado") String estado, HttpServletRequest request, RedirectAttributes att, Model model) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Cita> listcitas = citaService.getAll();
		listBuscarCita.clear();
		listcitas.forEach((cita) -> {
			String fechaCita = dateFormat.format(cita.getFecha());
			String fechaBuscar = dateFormat.format(fecha);
			if (fechaCita.equals(fechaBuscar) && cita.getEstado().equals(estado)) {
				listBuscarCita.add(cita);
				listBuscarCita.sort(Comparator.comparing(Cita::getHora));
				model.addAttribute("fecha", fechaBuscar);
				model.addAttribute("estado", estado);
			} else {
				model.addAttribute("fecha", fechaBuscar);
				model.addAttribute("estado", estado);
			}
		});
		model.addAttribute("evos", evolucionService.getAll());
		model.addAttribute("citas", listBuscarCita);
		model.addAttribute("admin", administradorService.get((String) request.getSession().getAttribute("admin_doc")));
		return "citaspaciente";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, Model model) {
		request.getSession().invalidate();
		return "redirect:/admin/login";
	}
}
