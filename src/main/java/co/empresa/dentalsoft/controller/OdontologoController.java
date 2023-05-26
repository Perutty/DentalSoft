package co.empresa.dentalsoft.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.AdministradorService;
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
	
	public static String uploadDirectory = "/home/centos/fotos";
	
	@GetMapping("/login")
	public String login(Model model) {
		return "loginodontologo";
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
	public String save(RedirectAttributes att,@RequestParam("file") MultipartFile foto,Odontologo odontologo, 
				Model model){
		String filename = foto.getOriginalFilename();
		Path fileNameAndPath = Paths.get(uploadDirectory,filename);
		
		try {
			Files.write(fileNameAndPath, foto.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		odontologo.setFoto(filename);
		odontologoService.save(odontologo);
		att.addFlashAttribute("accion", "¡Odontologo registrado con éxito!");
		return "redirect:/odontologo/list";
	}
	
	@GetMapping("/delete/{documento}")
	public String delete(RedirectAttributes att, @PathVariable("documento") String documento, Model model){
		odontologoService.delete(documento);
		att.addFlashAttribute("accion", "¡Odontologo eliminado con éxito!");
		return "redirect:/odontologo/list";
	}
}
