package co.empresa.dentalsoft.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import co.empresa.dentalsoft.model.Eps;
import co.empresa.dentalsoft.model.EstadoCivil;
import co.empresa.dentalsoft.model.Evolucion;
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.Pais;
import co.empresa.dentalsoft.model.Sexo;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EpsService;
import co.empresa.dentalsoft.service.EstadoCivilService;
import co.empresa.dentalsoft.service.EvolucionService;
import co.empresa.dentalsoft.service.HistoriaClinicaService;
import co.empresa.dentalsoft.service.OdontologoService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.service.PaisService;
import co.empresa.dentalsoft.service.SexoService;
import co.empresa.dentalsoft.service.TipoDocumentoService;
import co.empresa.dentalsoft.service.impl.CloudinaryService;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private PaisService paisService;

	@Autowired
	private HistoriaClinicaService historiaClinicaService;

	@Autowired
	private EpsService epsService;

	@Autowired
	private EstadoCivilService estadoCivilService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private TipoDocumentoService tipoDocumentoService;

	@Autowired
	private EvolucionService evolucionService;

	@Autowired
	private AdministradorService administradorService;

	@Autowired
	private CitaService citaService;
	
	@Autowired
	private OdontologoService odontologoService;

	@Autowired
	private CloudinaryService cloudinaryService;

	public List<Evolucion> ev = new ArrayList<>();

	public List<Cita> cita = new ArrayList<>();

	List<Cita> citas = new ArrayList<>();

	List<Cita> listBuscarCita = new ArrayList<>();

	boolean exist;

	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("paciente_doc") != null) {
			return "redirect:/paciente/dashboard";
		} else
			return "login";
	}

	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password,
			HttpServletRequest request, Model model) {

		if (pacienteService.select(documento, password) != null) {
			request.getSession().setAttribute("paciente_doc", documento);
			return "redirect:/paciente/dashboard";
		} else {
			att.addFlashAttribute("loginError", "Documento o contraseña incorrecta");
			return "redirect:/paciente/login";
		}
	}

	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model) {

		List<Cita> listCitas = citaService.getAll();
		citas.clear();

		listCitas.forEach((cita) -> {
			if (cita.getPaciente_doc().equals(pacienteService.get((String) request.getSession().getAttribute("paciente_doc")).getNombre()))
				citas.add(cita);
		});
		citas.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
		model.addAttribute("paciente", pacienteService.get((String) request.getSession().getAttribute("paciente_doc")));
		model.addAttribute("citas", citas);
		return "admindashboard";
	}

	@PostMapping("/save")
	public String save(RedirectAttributes att, Paciente paciente, @RequestParam MultipartFile file, Model model)
			throws IOException {

		List<Paciente> pacientes = pacienteService.getAll();
		pacientes.forEach((p) -> {
			if (p.getDocumento().equals(paciente.getDocumento())) {
				exist = true;
			}
		});
		if (exist == true) {
			att.addFlashAttribute("accion", "¡El documento de identidad ya se encuentra registrado!");
			return "redirect:/admin/dashboard";
		} else {

			paciente.setFoto(cloudinaryService.upload(file).get("url").toString());
			pacienteService.save(paciente);
			HistoriaClinica hc = new HistoriaClinica();
			hc.setPaciente_doc(paciente.getDocumento());
			historiaClinicaService.save(hc);
			att.addFlashAttribute("accion", "¡Paciente registrado con éxito!");
			return "redirect:/admin/dashboard";
		}
	}

	@GetMapping("/edit")
	public String editForPaciente(RedirectAttributes att, HttpServletRequest request, Model model) {

		model.addAttribute("tipoDoc", tipoDocumentoService.getAll());
		model.addAttribute("estadocivil", estadoCivilService.getAll());
		model.addAttribute("paciente", pacienteService.get((String) request.getSession().getAttribute("paciente_doc")));
		model.addAttribute("eps", epsService.getAll());
		model.addAttribute("sexo", sexoService.getAll());
		model.addAttribute("pais", paisService.getAll());
		return "editpaciente";
	}

	@PostMapping("/editDatos")
	public String editPaciente(RedirectAttributes att, Paciente paciente, HttpServletRequest request, Model model) {
		paciente.setFoto(pacienteService.get(paciente.getDocumento()).getFoto());
		att.addFlashAttribute("accion", "¡Datos del paciente actualizados con éxito!");
		return "redirect:/paciente/edit";
	}

	@PostMapping("/editFoto")
	public String editFoto(RedirectAttributes att, @RequestParam MultipartFile file,
			@RequestParam("documento") String documento, HttpServletRequest request, Model model) throws Exception {
		cloudinaryService.getImage(pacienteService.get(documento).getFoto());
		pacienteService.get(documento).setFoto(cloudinaryService.upload(file).get("url").toString());
		att.addFlashAttribute("accion", "¡Foto del perfil actualizada con éxito!");
		return "redirect:/paciente/edit";
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
			if (fechaCita.equals(fechaBuscar) && cita.getEstado().equals(estado)
					&& cita.getPaciente_doc().equals(pacienteService.get((String) request.getSession().getAttribute("paciente_doc")).getNombre())) {
				listBuscarCita.add(cita);
				listBuscarCita.sort(Comparator.comparing(Cita::getHora));
				model.addAttribute("fecha", fechaBuscar);
				model.addAttribute("estado", estado);
			} else {
				model.addAttribute("fecha", fechaBuscar);
				model.addAttribute("estado", estado);
			}
		});
		model.addAttribute("paciente", pacienteService.get((String) request.getSession().getAttribute("paciente_doc")));
		model.addAttribute("citas", listBuscarCita);
		return "pacientedashboard";
	}

	@GetMapping("/delete/{documento}")
	public String delete(RedirectAttributes att, @PathVariable("documento") String documento, Model model) {
		List<HistoriaClinica> hcs = historiaClinicaService.getAll();
		List<Evolucion> evo = evolucionService.getAll();
		hcs.forEach((hc) -> {
			if (hc.getPaciente_doc().equals(documento)) {
				evo.forEach((e) -> {
					if (e.getHistoria_id().equals(hc.getId())) {
						evolucionService.delete(e.getId());
					}
				});
				historiaClinicaService.delete(hc.getId());
			}
		});
		pacienteService.delete(documento);
		att.addFlashAttribute("accion", "¡Paciente eliminado con éxito!");
		return "redirect:/admin/dashboard";
	}

	@GetMapping("/odontograma/{documento}")
	public String historia(Model model, HttpServletRequest request, @PathVariable("documento") String documento) {
		model.addAttribute("admin", administradorService.get((String) request.getSession().getAttribute("admin_doc")));
		model.addAttribute("paci", pacienteService.get(documento));
		return "odontograma";
	}

	@PostMapping("/guardar-odontograma")
	public String saveOdontograma(RedirectAttributes att, HttpServletRequest request, @RequestParam MultipartFile file,
									@RequestParam String documento, Model model) throws IOException {
		
		pacienteService.get(documento).setOdontograma(cloudinaryService.upload(file).get("url").toString());
		att.addFlashAttribute("accion", "¡Odontograma guardado con éxito!");
		model.addAttribute("paci", pacienteService.get(documento));
		model.addAttribute("odonto", odontologoService.get((String)request.getSession().getAttribute("odonto_doc")).getNombre());
		model.addAttribute("admin", administradorService.get((String)request.getSession().getAttribute("admin_doc")).getNombre());
		return "admindashboard";
	}
	
	@GetMapping("/eliminar-odontograma/{odontograma}/{documento}")
	public String deleteOdontograma(@PathVariable("odontograma") String odontograma, @PathVariable("documento") String documento, Model model) throws Exception {
		cloudinaryService.getImage(pacienteService.get(documento).getOdontograma());
		pacienteService.get(documento).setOdontograma(null);
		return "redirect:/paciente/odontograma/"+documento;
	}

	@GetMapping("/tratamientos")
	public String tratamientos(Model model, HttpServletRequest request) {

		List<HistoriaClinica> historias = historiaClinicaService.getAll();
		List<Cita> citas = citaService.getAll();
		List<Evolucion> evos = evolucionService.getAll();
		ev.clear();
		cita.clear();
		historias.forEach((historia) -> {
			if (historia.getPaciente_doc().equals(pacienteService.get((String) request.getSession().getAttribute("paciente_doc")).getDocumento())) {
				evos.forEach((e) -> {
					if (e.getHistoria_id().equals(historia.getId())) {
						ev.add(e);
						citas.forEach((c) -> {
							if (c.getId().equals(e.getCita_id())) {
								cita.add(c);
							}
						});
					}
				});
			}
		});
		model.addAttribute("cita", cita);
		model.addAttribute("evos", ev);
		model.addAttribute("nombre", pacienteService.get((String) request.getSession().getAttribute("paciente_doc")).getNombre());
		model.addAttribute("paci", pacienteService.get((String) request.getSession().getAttribute("paciente_doc")));
		return "tratamientos";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, Model model) {
		request.getSession().invalidate();
		return "redirect:/paciente/login";
	}

}
