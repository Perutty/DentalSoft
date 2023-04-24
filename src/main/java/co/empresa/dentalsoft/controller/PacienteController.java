package co.empresa.dentalsoft.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import co.empresa.dentalsoft.service.PacienteService;

@Controller
@RequestMapping("/paciente")
public class PacienteController {
	
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpSession session, Model model) {
		if(request.getSession().getAttribute("paciente_doc") != null) {
			return "redirect:/paciente/dashboard";
		}else
			return "login";
	}

}
