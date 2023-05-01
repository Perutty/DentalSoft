package co.empresa.dentalsoft.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import co.empresa.dentalsoft.model.Eps;
import co.empresa.dentalsoft.model.EstadoCivil;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.Pais;
import co.empresa.dentalsoft.model.Sexo;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.EpsService;
import co.empresa.dentalsoft.service.EstadoCivilService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.service.PaisService;
import co.empresa.dentalsoft.service.SexoService;
import co.empresa.dentalsoft.service.TipoDocumentoService;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

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
	private EstadoCivilService estadoCivilService;
	
	@Autowired
	private SexoService sexoService;
	
	public static String uploadDirectory = "C:/home/centos/fotos/";
	
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpSession session, Model model) {
		
		if(request.getSession().getAttribute("admin_id") != null) {
			return "redirect:/admin/admindashboard";
		}else
			return "loginadmin";
	}
	
	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password, 
			HttpServletRequest request,  Model model) {
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
	
	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model){
		
		String adm_doc = (String)request.getSession().getAttribute("admin_doc");
		Administrador adm = administradorService.get(adm_doc);
		
		List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
		List<EstadoCivil> estadoCivil = estadoCivilService.getAll();
		List<Paciente> paciente = pacienteService.getAll();
		List<Pais> pais = paisService.getAll();
		List<Sexo> sexo = sexoService.getAll();
		List<Eps> eps = epsService.getAll();
		model.addAttribute("tipoDoc", tipoDoc);
		model.addAttribute("estadocivil", estadoCivil);
		model.addAttribute("paciente", paciente);
		model.addAttribute("eps", eps);
		model.addAttribute("sexo", sexo);
		model.addAttribute("pais", pais);
		model.addAttribute("admin", adm);
		return "admindashboard";
	}
	
	@GetMapping("/edit/{documento}")
	public String edit(RedirectAttributes att,@PathVariable("documento") String documento, HttpServletRequest request,Model model){
		Administrador admin = administradorService.get(documento);
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
	
	@PostMapping("/editFoto")
	public String editFoto(RedirectAttributes att, @RequestParam("file") MultipartFile foto, HttpServletRequest request,Model model)
	{
		String adm_doc = (String)request.getSession().getAttribute("admin_doc");
		Administrador adm = administradorService.get(adm_doc);
		
		String filename = adm.getDocumento() + foto.getOriginalFilename().substring(foto.getOriginalFilename().length()-4);
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
			adm.setFoto(filename);
			administradorService.save(adm);
			att.addFlashAttribute("accion", "¡Foto del perfil actualizada con éxito!");
		
		return "redirect:/admin/edit/"+adm.getDocumento();
	}
	
	@PostMapping("/editAdmin")
	public String editDatos(RedirectAttributes att, @RequestParam("documento") String documento, @RequestParam("nombre") String nombre,
			@RequestParam("tipodoc") String tipodoc, @RequestParam("correo") String correo, @RequestParam("celular") String celular, 
			@RequestParam("password") String password, HttpServletRequest request,Model model)
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
		return "redirect:/admin/dashboard";
	}
	
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpSession session,  Model model) {
			request.getSession().invalidate();
			return "redirect:/admin/login";
	}
}
