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
import co.empresa.dentalsoft.service.impl.CloudinaryService;

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
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	List<Cita> citas = new ArrayList<>();
	
	boolean exist;
	
	List<Cita> listBuscarCita = new ArrayList<>();
	List<Paciente> listBuscarPaciente = new ArrayList<>();
	public List<Evolucion> ev = new ArrayList<>();
	public List<Cita> cita = new ArrayList<>();
	List<Evolucion> listEvo = new ArrayList<>();
	
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("odonto_doc") != null) {
			return "redirect:/odontologo/dashboard";
		} else
		return "loginodontologo";
	}
	
	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password, HttpServletRequest request, Model model) {
		
		if(odontologoService.select(documento, password) != null)
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
			
		List<Cita> listCitas = citaService.getAll();
		citas.clear();
		listCitas.forEach((cita)->{
			if(cita.getOdontologo_doc().equals(odontologoService.get((String)request.getSession().getAttribute("odonto_doc")).getNombre()))
				citas.add(cita);
		});
		citas.sort(Comparator.comparing(Cita::getFecha).reversed().thenComparing(Cita::getHora));
		model.addAttribute("evos", evolucionService.getAll());
		model.addAttribute("odonto", odontologoService.get((String)request.getSession().getAttribute("odonto_doc")));
		model.addAttribute("citas", citas);
			return "admindashboard";
	}
	
	@GetMapping("/list")
	public String list(HttpServletRequest request, Model model){
		model.addAttribute("tipoDoc", tipoDocumentoService.getAll());
		model.addAttribute("odontologos", odontologoService.getAll());
		model.addAttribute("admin", administradorService.get((String)request.getSession().getAttribute("admin_doc")));
		return "misodontologos";
	}
	
	@PostMapping("/save")
	public String save(RedirectAttributes att,@RequestParam MultipartFile file,Odontologo odontologo, 
				Model model) throws IOException{
		
		odontologoService.getAll().forEach((o) ->{
			if(o.getDocumento().equals(odontologo.getDocumento())) {
				exist = true;
			}
		});
		
		if(exist == true) {
			att.addFlashAttribute("accion", "¡Documento de identidad ya se encuentra registrado!");
			return "redirect:/odontologo/list";
		}else {
			odontologo.setFoto(cloudinaryService.upload(file).get("url").toString());
			odontologoService.save(odontologo);
			att.addFlashAttribute("accion", "¡Odontologo registrado con éxito!");
			return "redirect:/odontologo/list";
		}
		
	}
	
	@GetMapping("/edit/{documento}")
	public String edit(RedirectAttributes att, HttpServletRequest request, @PathVariable("documento") String documento, Model model){
		model.addAttribute("tipoDoc", tipoDocumentoService.getAll());
		model.addAttribute("odontologo", odontologoService.get(documento));
		model.addAttribute("administrador", administradorService.get((String)request.getSession().getAttribute("admin_doc")));
		model.addAttribute("isAdmin", true);
		return "editpaciente";
	}
	
	@GetMapping("/buscar")
	public String buscarCitas(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha, 
								@RequestParam("estado") String estado, HttpServletRequest request, 
								RedirectAttributes att,Model model) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Cita> listcitas = citaService.getAll();
		listBuscarCita.clear();
		listcitas.forEach((cita) -> {
			String fechaCita = dateFormat.format(cita.getFecha());
			String fechaBuscar = dateFormat.format(fecha);
		if(fechaCita.equals(fechaBuscar) && cita.getEstado().equals(estado) && cita.getOdontologo_doc().equals(odontologoService.get((String)request.getSession().getAttribute("odonto_doc")).getNombre())) {
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
		model.addAttribute("evos", evolucionService.getAll());
		model.addAttribute("odontologo", odontologoService.get((String)request.getSession().getAttribute("odonto_doc")));
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
		List<Cita> listcitas = citaService.getAll();
		List<Paciente> pacientes = pacienteService.getAll();
		listBuscarPaciente.clear();
		listcitas.forEach((c)->{
			pacientes.forEach((p)->{
				if(c.getOdontologo_doc().equals(odontologoService.get((String)request.getSession().getAttribute("odonto_doc")).getNombre()) && c.getPaciente_doc().equals(p.getNombre()))
							if(!listBuscarPaciente.contains(p))
							listBuscarPaciente.add(p);
					});
		});
		listBuscarPaciente.sort(Comparator.comparing(Paciente::getNombre));
		model.addAttribute("odontologo", odontologoService.get((String)request.getSession().getAttribute("odonto_doc")));
		model.addAttribute("pacientes", listBuscarPaciente);
		return "odontologopacientes";
	}
	
	@GetMapping("/verpaciente/{documento}")
	public String verPaciente(Model model, HttpServletRequest request,@PathVariable("documento") String documento) {
		model.addAttribute("tipoDoc", tipoDocumentoService.getAll());
		model.addAttribute("estadocivil", estadoCivilService.getAll());
		model.addAttribute("eps", epsService.getAll());
		model.addAttribute("sexo", sexoService.getAll());
		model.addAttribute("pais",  paisService.getAll());
		model.addAttribute("paciente", pacienteService.get(documento));
		model.addAttribute("odontologo", odontologoService.get((String)request.getSession().getAttribute("odonto_doc")));
		return "verpaciente";
	}
	
	@GetMapping("/evolucion/new/{idCita}/{documento}")
	public String generarEvo(RedirectAttributes att, HttpServletRequest request, Model model, @PathVariable("idCita") Integer idCita,
			@PathVariable("documento") String documento) {
			
			List<Evolucion> evos = evolucionService.getAll();
			List<HistoriaClinica> hc = historiaClinicaService.getAll();
			listEvo.clear();
			List<Paciente> pacientes = pacienteService.getAll();
			evos.forEach((e)->{
				hc.forEach((h)->{
					pacientes.forEach((p)->{
						if(p.getNombre().equals(documento) && h.getPaciente_doc().equals(p.getDocumento())) {
							model.addAttribute("historia", historiaClinicaService.get(h.getId()));
						}
						
						if(e.getCita_id().equals(idCita)) {
							if(h.getPaciente_doc().equals(p.getNombre())) {
								if(e.getCita_id().equals(idCita) && e.getHistoria_id().equals(h.getId())) {
									listEvo.add(e);
									
								}
							}
						}
					});
				});
			});
			if(listEvo.isEmpty()) {
			model.addAttribute("cita", citaService.get(idCita));
			model.addAttribute("odontologo", odontologoService.get((String)request.getSession().getAttribute("odonto_doc")));
				return "evolucionodonto";
			}else {
				att.addFlashAttribute("accion", "¡Esta cita ya cuenta con una evolución!");
				return "redirect:/odontologo/dashboard/";
			}
	}
	
	@PostMapping("/saveevo")
	public String save(RedirectAttributes att, Model model, @RequestParam  String historia_id, @RequestParam String cita_id, 
						@RequestParam String descripcion) {
		Evolucion evo = new Evolucion();
		evo.setCita_id(Integer.valueOf(cita_id));
		evo.setHistoria_id(Integer.valueOf(historia_id));
		evo.setDescripcion(descripcion);
		evolucionService.save(evo);
		att.addFlashAttribute("accion", "¡Evolución agregada con éxito!");
		return "redirect:/odontologo/dashboard/";
	}
	
	@GetMapping("/verhistoria/{documento}")
	public String list(HttpServletRequest request,  @PathVariable("documento") String documento, Model model){
		
		List<HistoriaClinica> historias = historiaClinicaService.getAll();
		List<Cita> citas = citaService.getAll();
		List<Evolucion> evos = evolucionService.getAll();
		ev.clear();
		cita.clear();
		historias.forEach((historia)->{
			if(historia.getPaciente_doc().equals(pacienteService.get(documento).getDocumento())){
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
		model.addAttribute("nombre", pacienteService.get(documento).getNombre());
		model.addAttribute("paci", pacienteService.get(documento));
		model.addAttribute("odontologo", odontologoService.get((String)request.getSession().getAttribute("odonto_doc")));
		return "historiapaciente";
	}
	
	@GetMapping("/odontograma/{documento}")
	public String historia(Model model,@PathVariable("documento") String documento, HttpServletRequest request) {
		model.addAttribute("odonto", odontologoService.get((String)request.getSession().getAttribute("odonto_doc")));
		model.addAttribute("paci", pacienteService.get(documento));
		return "odontograma";
	}
}
