package co.empresa.dentalsoft.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.TipoDocumentoService;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

	@Autowired
	private AdministradorService administradorService;
	
	@Autowired
	private TipoDocumentoService tipoDocumentoService;
	
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpSession session, Model model) {
		if(request.getSession().getAttribute("admin_id") != null) {
			return "redirect:/admin/admindashboard";
		}else
			return "loginadmin";
	}
	
	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password, 
			HttpServletRequest request, HttpSession session,  Model model) {
		Administrador admin = administradorService.select(documento, password);
		
		if(admin != null)
		{
			request.getSession().setAttribute("admin", admin.getNombre());
			return "redirect:/admin/dashboard";
		}else {
			att.addFlashAttribute("loginError", "Documento o contraseña incorrecta");
			return "redirect:/admin/login";
			}
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model){
		String adm_nombre = (String)request.getSession().getAttribute("admin");
		List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
		model.addAttribute("tipoDoc", tipoDoc);
		model.addAttribute("admin", adm_nombre);
		return "admindashboard";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpSession session,  Model model) {
			request.getSession().invalidate();
			return "redirect:/admin/login";
	}
}
