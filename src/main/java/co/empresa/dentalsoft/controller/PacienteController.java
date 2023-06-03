package co.empresa.dentalsoft.controller;

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
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.Pais;
import co.empresa.dentalsoft.model.Sexo;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EpsService;
import co.empresa.dentalsoft.service.EstadoCivilService;
import co.empresa.dentalsoft.service.HistoriaClinicaService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.service.PaisService;
import co.empresa.dentalsoft.service.SexoService;
import co.empresa.dentalsoft.service.TipoDocumentoService;

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
	private AdministradorService administradorService;
	
	
	@Autowired
	private CitaService citaService;
	
	public static String uploadDirectory = "/home/centos/fotos";
	
	List<Cita> citas = new ArrayList<>();
	
	List<Cita> listBuscarCita = new ArrayList<>();

	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		if(request.getSession().getAttribute("paciente_doc") != null) {
			return "redirect:/paciente/dashboard";
		}else 
			return "login";
	}
	
	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password, HttpServletRequest request, Model model) {
		
		Paciente paciente = pacienteService.select(documento, password);
		if(paciente != null)
		{
			request.getSession().setAttribute("paciente_doc", documento);
			return "redirect:/paciente/dashboard";
		}else {
			att.addFlashAttribute("loginError", "Documento o contraseña incorrecta");
			return "redirect:/paciente/login";
			}
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model){
			
		Paciente paci = pacienteService.get((String)request.getSession().getAttribute("paciente_doc"));
		List<Cita> listCitas = citaService.getAll();
		citas.clear();
		
		listCitas.forEach((cita)->{
			if(cita.getPaciente_doc().equals(paci.getNombre()))
				citas.add(cita);
		});
		citas.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
		model.addAttribute("paciente", paci);
		model.addAttribute("citas", citas);
			return "pacientedashboard";
	}
	
	@PostMapping("/save")
	public String save(RedirectAttributes att,@RequestParam("file") MultipartFile foto,Paciente paciente, 
				Model model){
		
				String filename = foto.getOriginalFilename();
				Path fileNameAndPath = Paths.get(uploadDirectory,filename);
				
				try {
					Files.write(fileNameAndPath, foto.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				paciente.setFoto(filename);
				pacienteService.save(paciente);
				HistoriaClinica hc = new HistoriaClinica();
				hc.setPaciente_doc(paciente.getDocumento());
				historiaClinicaService.save(hc);
				att.addFlashAttribute("accion", "¡Paciente registrado con éxito!");
		return "redirect:/admin/dashboard";
}
	
	@GetMapping("/edit")
	public String editForPaciente(RedirectAttributes att, HttpServletRequest request, Model model){
		
		Paciente paci = pacienteService.get((String)request.getSession().getAttribute("paciente_doc"));
		
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
		return "pacienteedit";
	}
	
	@PostMapping("/editDatos")
	public String editPaciente(RedirectAttributes att, Paciente paciente, HttpServletRequest request, Model model){
		Paciente paci = pacienteService.get(paciente.getDocumento());
		paciente.setFoto(paci.getFoto());
		pacienteService.save(paciente);
		att.addFlashAttribute("accion", "¡Datos del paciente actualizados con éxito!");
		return "redirect:/paciente/edit";
	}
	
	@PostMapping("/editFoto")
	public String editFoto(RedirectAttributes att, @RequestParam("file") MultipartFile foto, 
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
		return "redirect:/paciente/edit";
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
	
	@GetMapping("/delete/{documento}")
	public String delete(RedirectAttributes att, @PathVariable("documento") String documento, Model model){
		pacienteService.delete(documento);
		att.addFlashAttribute("accion", "¡Paciente eliminado con éxito!");
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/historia")
	public String historia(Model model, HttpServletRequest request) {
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		model.addAttribute("admin", adm);
		return "odontograma";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, Model model) {
			request.getSession().invalidate();
			return "redirect:/paciente/login";
	}

}
