package co.empresa.dentalsoft.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.service.AdministradorService;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

	@Autowired
	private AdministradorService administradorService;
	
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpSession session, Model model) {
		if(request.getSession().getAttribute("admin_id") != null) {
			return "redirect:/producto/list";
		}else
			return "loginadmin";
	}
	
	@PostMapping("/signin")
	public String validate(RedirectAttributes att, @RequestParam String documento, @RequestParam String password, 
			HttpServletRequest request, HttpSession session,  Model model) {
		Administrador admin = administradorService.select(documento, password);
		
		if(admin != null)
		{
			request.getSession().setAttribute("documento", admin.getDocumento());
			return "redirect:/producto/list";
		}else {
			att.addFlashAttribute("loginError", "documento o contraseña incorrecta");
			return "redirect:/dentalsoft/login";
			}
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpSession session,  Model model) {
			request.getSession().invalidate();
			return "redirect:/admin/";
	}
}
