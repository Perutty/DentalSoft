package co.empresa.dentalsoft.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import co.empresa.dentalsoft.model.Odontologo;
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

@Controller
@RequestMapping("/odontologo")
public class OdontologoController {

	@Autowired
	private OdontologoService odontologoService;
	
	@Autowired
	private AdministradorService administradorService;
	
	@Autowired
	private TipoDocumentoService tipoDocumentoService;
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private EpsService epsService;
	
	@Autowired
	private SexoService sexoService;
	
	@Autowired
	private HistoriaClinicaService historiaClinicaService;
	
	@Autowired
	private EvolucionService evolucionService;
	
	@Autowired
	private EstadoCivilService estadoCivilService;
	
	@Autowired
	private CitaService citaService;
	
	List<Cita> citas = new ArrayList<>();
	
	boolean exist;
	
	List<Cita> listBuscarCita = new ArrayList<>();
	List<Paciente> listBuscarPaciente = new ArrayList<>();
	public List<Evolucion> ev = new ArrayList<>();
	
	public List<Cita> cita = new ArrayList<>();
	
	public static String uploadDirectory = "/home/centos/fotos";
	
	@GetMapping("/login")
	public String login(Model model) {
		return "loginodontologo";
	}
	
	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password, HttpServletRequest request, Model model) {
		
		Odontologo odonto = odontologoService.select(documento, password);
		if(odonto != null)
		{
			request.getSession().setAttribute("odonto_doc", documento);
			return "redirect:/odontologo/dashboard";
		}else {
			att.addFlashAttribute("loginError", "Documento o contraseña incorrecta");
			return "redirect:/odontologo/login";
			}
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model){
			
		Odontologo odonto = odontologoService.get((String)request.getSession().getAttribute("odonto_doc"));
		List<Cita> listCitas = citaService.getAll();
		citas.clear();
		
		listCitas.forEach((cita)->{
			if(cita.getOdontologo_doc().equals(odonto.getNombre()))
				citas.add(cita);
		});
		citas.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
		model.addAttribute("odontologo", odonto);
		model.addAttribute("citas", citas);
			return "odontologodashboard";
	}
	
	@GetMapping("/list")
	public String list(HttpServletRequest request, Model model){
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
		List<Odontologo> odontologos = odontologoService.getAll();
		model.addAttribute("tipoDoc", tipoDoc);
		model.addAttribute("odontologos", odontologos);
		model.addAttribute("admin", adm);
		return "misodontologos";
	}
	
	@PostMapping("/save")
	public String save(RedirectAttributes att,@RequestParam("imagen") String imagen,Odontologo odontologo, 
				Model model){
		List<Odontologo> odontologos = odontologoService.getAll();
		
		/*String filename = foto.getOriginalFilename();
		Path fileNameAndPath = Paths.get(uploadDirectory,filename);
		
		try {
			Files.write(fileNameAndPath, foto.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		odontologos.forEach((o) ->{
			if(o.getDocumento().equals(odontologo.getDocumento())) {
				exist = true;
			}
		});
		
		if(exist == true) {
			att.addFlashAttribute("accion", "¡Documento de identidad ya se encuentra registrado!");
			return "redirect:/odontologo/list";
		}else {
			odontologo.setFoto(imagen);
			odontologoService.save(odontologo);
			att.addFlashAttribute("accion", "¡Odontologo registrado con éxito!");
			return "redirect:/odontologo/list";
		}
		
	}
	
	@GetMapping("/edit/{documento}")
	public String edit(RedirectAttributes att, HttpServletRequest request, @PathVariable("documento") String documento, Model model){
		String adm_doc = (String)request.getSession().getAttribute("admin_doc");
		Administrador adm = administradorService.get(adm_doc);
		Odontologo odonto = odontologoService.get(documento);
		
		List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
		
		model.addAttribute("tipoDoc", tipoDoc);
		model.addAttribute("odontologo", odonto);
		model.addAttribute("admin", adm);
		return "editodontologo";
	}
	
	@GetMapping("/buscar")
	public String buscarCitas(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha, 
								@RequestParam("estado") String estado, HttpServletRequest request, 
								RedirectAttributes att,Model model) {
		Odontologo odonto = odontologoService.get((String)request.getSession().getAttribute("odonto_doc"));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Cita> listcitas = citaService.getAll();
		listBuscarCita.clear();
		listcitas.forEach((cita) -> {
			String fechaCita = dateFormat.format(cita.getFecha());
			String fechaBuscar = dateFormat.format(fecha);
		if(fechaCita.equals(fechaBuscar) && cita.getEstado().equals(estado) && cita.getOdontologo_doc().equals(odonto.getNombre())) {
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
		model.addAttribute("odontologo", odonto);
		model.addAttribute("citas", listBuscarCita);
		return "odontologodashboard";
	}
	
	@GetMapping("/delete/{documento}")
	public String delete(RedirectAttributes att, @PathVariable("documento") String documento, Model model){
		odontologoService.delete(documento);
		att.addFlashAttribute("accion", "¡Odontologo eliminado con éxito!");
		return "redirect:/odontologo/list";
	}
	
	@GetMapping("/atender/{id}")
	public String atenderCita(RedirectAttributes att, @PathVariable("id") String id, HttpServletRequest request, Model model) {
		
		Cita cita = citaService.get(Integer.parseInt(id));
		cita.setEstado("Finalizada");
		citaService.save(cita);
		att.addFlashAttribute("accion", "¡Cita finalizada con éxito!");
		return "redirect:/odontologo/dashboard";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, Model model) {
			request.getSession().invalidate();
			return "redirect:/odontologo/login";
	}
	
	@GetMapping("/pacientes")
	public String pacientes(Model model, HttpServletRequest request) {
		Odontologo odonto = odontologoService.get((String)request.getSession().getAttribute("odonto_doc"));
		List<Cita> listcitas = citaService.getAll();
		List<Paciente> pacientes = pacienteService.getAll();
		listBuscarPaciente.clear();
		listcitas.forEach((c)->{
			pacientes.forEach((p)->{
				if(c.getOdontologo_doc().equals(odonto.getNombre()) && c.getPaciente_doc().equals(p.getNombre()))
							if(!listBuscarPaciente.contains(p))
							listBuscarPaciente.add(p);
					});
		});
		listBuscarPaciente.sort(Comparator.comparing(Paciente::getNombre));
		model.addAttribute("odontologo", odonto);
		model.addAttribute("pacientes", listBuscarPaciente);
		return "odontologopacientes";
	}
	
	@GetMapping("/verpaciente/{documento}")
	public String verPaciente(Model model, HttpServletRequest request,@PathVariable("documento") String documento) {
		Odontologo odonto = odontologoService.get((String)request.getSession().getAttribute("odonto_doc"));
		Paciente paci = pacienteService.get(documento);
		List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
		List<EstadoCivil> estadoCivil = estadoCivilService.getAll();
		List<Pais> pais = paisService.getAll();
		List<Sexo> sexo = sexoService.getAll();
		List<Eps> eps = epsService.getAll();
		model.addAttribute("tipoDoc", tipoDoc);
		model.addAttribute("estadocivil", estadoCivil);
		model.addAttribute("paciente", paci);
		model.addAttribute("eps", eps);
		model.addAttribute("sexo", sexo);
		model.addAttribute("pais", pais);
		model.addAttribute("paciente", paci);
		model.addAttribute("odontologo", odonto);
		return "verpaciente";
	}
	
	@GetMapping("/verhistoria/{documento}")
	public String list(HttpServletRequest request,  @PathVariable("documento") String documento, Model model){
		Odontologo odonto = odontologoService.get((String)request.getSession().getAttribute("odonto_doc"));
		
		Paciente paci = pacienteService.get(documento);
		
		List<HistoriaClinica> historias = historiaClinicaService.getAll();
		List<Cita> citas = citaService.getAll();
		List<Evolucion> evos = evolucionService.getAll();
		ev.clear();
		cita.clear();
		historias.forEach((historia)->{
			if(historia.getPaciente_doc().equals(paci.getDocumento())){
				evos.forEach((e)->{
					if(e.getHistoria_id().equals(historia.getId()))
					{
						ev.add(e);
						citas.forEach((c)->{
							if(c.getId().equals(e.getCita_id())) {
								cita.add(c);
							}
						});
					}
				});
			}
		});
		model.addAttribute("cita", cita);
		model.addAttribute("evos", ev);
		model.addAttribute("nombre", paci.getNombre());
		model.addAttribute("paci", paci);
		model.addAttribute("odontologo", odonto);
		return "historiapaciente";
	}
}
