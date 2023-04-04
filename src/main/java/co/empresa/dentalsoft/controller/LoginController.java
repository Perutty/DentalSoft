package co.empresa.dentalsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dentalsoft")
public class LoginController {

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
}
