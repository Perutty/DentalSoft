package co.empresa.dentalsoft.controller;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.Cita;
import co.empresa.dentalsoft.model.Eps;
import co.empresa.dentalsoft.model.EstadoCivil;
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.model.Hora;
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.Pais;
import co.empresa.dentalsoft.model.Sexo;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.model.Tratamiento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EpsService;
import co.empresa.dentalsoft.service.EstadoCivilService;
import co.empresa.dentalsoft.service.HistoriaClinicaService;
import co.empresa.dentalsoft.service.HoraService;
import co.empresa.dentalsoft.service.OdontologoService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.service.PaisService;
import co.empresa.dentalsoft.service.SexoService;
import co.empresa.dentalsoft.service.TipoDocumentoService;
import co.empresa.dentalsoft.service.TratamientoService;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

	@Autowired
	private AdministradorService administradorService;
	
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
	private SexoService sexoService;
	
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/images";
	
	List<Cita> listCitasByPaciente = new ArrayList<>();
	List<Cita> citasHistorial = new ArrayList<>();
	List<HistoriaClinica> listaHistoria = new ArrayList<>();
	HistoriaClinica historia = new HistoriaClinica();
	List<Cita> listBuscarCita = new ArrayList<>();
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		
		if(request.getSession().getAttribute("admin_doc") != null) {
			return "redirect:/admin/admindashboard";
		}else
			return "loginadmin";
	}
	
	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password, HttpServletRequest request, Model model) {
		
		Administrador admin = administradorService.select(documento, password);
		if(admin != null)
		{
			request.getSession().setAttribute("admin_doc", documento);
			return "redirect:/admin/dashboard";
		}else {
			att.addFlashAttribute("loginError", "Documento o contraseña incorrecta");
			return "redirect:/admin/login";
			}
	}
	
	@GetMapping("/edit/{documento}")
	public String editForAdmin(RedirectAttributes att, HttpServletRequest request, @PathVariable("documento") String documento, Model model){
		String adm_doc = (String)request.getSession().getAttribute("admin_doc");
		Administrador adm = administradorService.get(adm_doc);
		
		List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
		List<EstadoCivil> estadoCivil = estadoCivilService.getAll();
		Paciente paciente = pacienteService.get(documento);
		
		List<Pais> pais = paisService.getAll();
		List<Sexo> sexo = sexoService.getAll();
		List<Eps> eps = epsService.getAll();
		model.addAttribute("tipoDoc", tipoDoc);
		model.addAttribute("estadocivil", estadoCivil);
		model.addAttribute("paciente", paciente);
		model.addAttribute("eps", eps);
		model.addAttribute("sexo", sexo);
		model.addAttribute("pais", pais);
		model.addAttribute("paci", paciente);
		model.addAttribute("admin", adm);
		return "editpaciente";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model){
			
			Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
			
			List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
			List<EstadoCivil> estadoCivil = estadoCivilService.getAll();
			List<Paciente> paciente = pacienteService.getAll();
			List<Pais> pais = paisService.getAll();
			List<Sexo> sexo = sexoService.getAll();
			List<Eps> eps = epsService.getAll();
			paciente.sort(Comparator.comparing(Paciente::getNombre));
			model.addAttribute("tipoDoc", tipoDoc);
			model.addAttribute("estadocivil", estadoCivil);
			model.addAttribute("paciente", paciente);
			model.addAttribute("eps", eps);
			model.addAttribute("sexo", sexo);
			model.addAttribute("pais", pais);
			model.addAttribute("admin", adm);
			
			return "admindashboard";
	}
	
	@GetMapping("/citas/{documento}")
	public String citas(HttpServletRequest request, @PathVariable("documento") String documento, Model model){
		
			Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
			Paciente paci = pacienteService.get(documento);
			List<HistoriaClinica> listHC = historiaClinicaService.getAll();
			request.getSession().setAttribute("docPaci", documento);
			List<Cita> listCitas = citaService.getAll();
			List<Tratamiento> tratamientos = tratamientoService.getAll();
			List<Odontologo> odontologos = odontologoService.getAll();
			List<Hora> horas = horaService.getAll();
			listCitasByPaciente.clear();
			listCitas.forEach((cita)->{
				if(cita.getPaciente_doc().equals(paci.getNombre()))
					listCitasByPaciente.add(cita);
			});
			listHC.forEach((hc)->{
				if(hc.getPaciente_doc().equals(paci.getDocumento())) 
					historia = historiaClinicaService.get(hc.getId());
				
			});
			
			listCitasByPaciente.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
			model.addAttribute("hc", historia.getId());
			model.addAttribute("tratamientos", tratamientos);
			model.addAttribute("horas", horas);
			model.addAttribute("odontologos", odontologos);
			model.addAttribute("nombre", paci.getNombre());
			model.addAttribute("paci", paci);
			model.addAttribute("citas", listCitasByPaciente);
			model.addAttribute("admin", adm);
			return "citaspaciente";
	}
	
	@GetMapping("/edit")
	public String edit(HttpServletRequest request, Model model){
		String adm_doc = (String)request.getSession().getAttribute("admin_doc");
		Administrador admin = administradorService.get(adm_doc);
		List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
		model.addAttribute("tipoDoc", tipoDoc);	
		model.addAttribute("adm", admin);
		return "editadmin";
	}
	
	@PostMapping("/save")
	public String save(RedirectAttributes att, Administrador administrador, Model model) {
		if(administrador != null) {
			administradorService.save(administrador);
			att.addFlashAttribute("accion", "¡Datos actualizados con éxito!");
		}
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(RedirectAttributes att, @PathVariable("id") String id, Model model){
		citaService.delete(Integer.parseInt(id));
		att.addFlashAttribute("accion", "¡Cita eliminada con éxito!");
		return "redirect:/cita/list";
	}
	
	@PostMapping("/editFotoAdmin")
	public String editFotoAdmin(RedirectAttributes att, @RequestParam("file") MultipartFile foto,HttpServletRequest request,Model model) throws IOException
	{
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		
		String filename = foto.getOriginalFilename();
		Path fileNameAndPath = Paths.get(uploadDirectory, filename);
		
		String fotoAntigua = adm.getFoto();
		File borrar = new File(uploadDirectory,fotoAntigua);
		
		if(borrar.exists())
			borrar.delete();
		
		
		try {
			Files.write(fileNameAndPath, foto.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
			
			adm.setFoto(foto.getOriginalFilename());
			administradorService.save(adm);
			att.addFlashAttribute("accion", "¡Foto del perfil actualizada con éxito!");
		
		return "redirect:/admin/edit";
	}
	
	@PostMapping("/editDatosAdmin")
	public String editDatosAdmin(RedirectAttributes att, @RequestParam("documento") String documento, @RequestParam("nombre") String nombre,
			@RequestParam("tipodoc") String tipodoc, @RequestParam("correo") String correo, @RequestParam("celular") String celular, 
			@RequestParam("password") String password,Model model)
	{
		Administrador adm = administradorService.get(documento);
		adm.setNombre(nombre);
		adm.setTipodoc(tipodoc);
		adm.setCelular(celular);
		adm.setCorreo(correo);
		adm.setDocumento(documento);
		adm.setPassword(password);
		adm.setFoto(adm.getFoto());
		administradorService.save(adm);
		att.addFlashAttribute("accion", "¡Datos personales actualizados con éxito!");
		return "redirect:/admin/edit";
	}
	
	@PostMapping("/editDatosPaciente")
	public String editPaciente(RedirectAttributes att, Paciente paciente, HttpServletRequest request, Model model){
		List<Cita> citas = citaService.getAll();
		Paciente paci = pacienteService.get(paciente.getDocumento());
		citas.forEach((cita) -> {
			if(cita.getPaciente_doc().equals(paci.getNombre())) {
				cita.setPaciente_doc(paciente.getNombre());
				citaService.save(cita);
			}
		});
		paciente.setFoto(paci.getFoto());
		pacienteService.save(paciente);
		att.addFlashAttribute("accion", "¡Datos del paciente actualizados con éxito!");
		return "redirect:/admin/edit/"+paci.getDocumento();
	}
	
	@PostMapping("/editDatosOdontologo")
	public String editOdontologo(RedirectAttributes att, Odontologo odontologo, HttpServletRequest request, Model model)
	{
		Odontologo odonto = odontologoService.get(odontologo.getDocumento());
		odontologo.setFoto(odonto.getFoto());
		odontologoService.save(odontologo);
		att.addFlashAttribute("accion", "¡Datos del paciente actualizados con éxito!");
		return "redirect:/odontologo/edit/"+odonto.getDocumento();
	}
	
	@PostMapping("/editFotoPaciente")
	public String editFotoPaciente(RedirectAttributes att, @RequestParam("file") MultipartFile foto, 
							@RequestParam("documento") String documento, HttpServletRequest request,Model model)
	{
		Paciente paciente = pacienteService.get(documento);
		String filename = foto.getOriginalFilename(); 
		Path fileNameAndPath = Paths.get(uploadDirectory, filename);
		String fotoAntigua = paciente.getFoto();
		File borrar = new File(uploadDirectory,fotoAntigua);
		
		if(borrar.exists())
			borrar.delete();
		
		try {
			Files.write(fileNameAndPath, foto.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		paciente.setFoto(filename);
		pacienteService.save(paciente);
		att.addFlashAttribute("accion", "¡Foto del perfil actualizada con éxito!");
		return "redirect:/admin/edit/"+paciente.getDocumento();
	}
	
	@PostMapping("/editFotoOdontologo")
	public String editFotoOdontologo(RedirectAttributes att, @RequestParam("file") MultipartFile foto, 
							@RequestParam("documento") String documento, HttpServletRequest request,Model model){
		
		Odontologo odonto = odontologoService.get(documento);
		String filename = foto.getOriginalFilename(); 
		Path fileNameAndPath = Paths.get(uploadDirectory, filename);
		String fotoAntigua = odonto.getFoto();
		File borrar = new File(uploadDirectory,fotoAntigua);
		
		if(borrar.exists())
			borrar.delete();
		
		try {
			Files.write(fileNameAndPath, foto.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		odonto.setFoto(filename);
		odontologoService.save(odonto);
		att.addFlashAttribute("accion", "¡Foto del perfil actualizada con éxito!");
		return "redirect:/odontologo/edit/"+odonto.getDocumento();
	}
	
	@GetMapping("/buscar")
	public String buscarCitas(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha, 
								@RequestParam("estado") String estado, HttpServletRequest request, 
								RedirectAttributes att,Model model) {
		
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Cita> listcitas = citaService.getAll();
		listBuscarCita.clear();
		listcitas.forEach((cita) -> {
			String fechaCita = dateFormat.format(cita.getFecha());
			String fechaBuscar = dateFormat.format(fecha);
		if(fechaCita.equals(fechaBuscar) && cita.getEstado().equals(estado)) {
				listBuscarCita.add(cita);
				listBuscarCita.sort(Comparator.comparing(Cita::getHora));	
				model.addAttribute("fecha", fechaBuscar);
				model.addAttribute("estado", estado);
		}else
		{
			model.addAttribute("fecha", fechaBuscar);
			model.addAttribute("estado", estado);
		}
		});
		model.addAttribute("citas", listBuscarCita);
		model.addAttribute("admin", adm);
		return "citaspaciente";
	}
	
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, Model model) {
			request.getSession().invalidate();
			return "redirect:/admin/login";
	}
}
