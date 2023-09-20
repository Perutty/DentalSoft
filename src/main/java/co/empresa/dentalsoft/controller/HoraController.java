package co.empresa.dentalsoft.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import co.empresa.dentalsoft.model.Hora;
import co.empresa.dentalsoft.service.HoraService;

@Controller
@RequestMapping("/hora")
public class HoraController {
	
	@Autowired
	private HoraService horaService;
	
	List<String> horas = new ArrayList<>();
	
	@GetMapping("/list")
	@ResponseBody
	public String list() {
		horas.clear();
		List<Hora> hrs = horaService.getAll();
		hrs.forEach((h)->{
			horas.add(h.getHora());
		});
		return horas.toString();
	}

}
