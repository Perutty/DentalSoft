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
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.OdontologoService;
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
	private CitaService citaService;
	
	List<Cita> citas = new ArrayList<>();
	
	boolean exist;
	
	List<Cita> listBuscarCita = new ArrayList<>();
	
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
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, Model model) {
			request.getSession().invalidate();
			return "redirect:/odontologo/login";
	}
}
