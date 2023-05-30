package co.empresa.dentalsoft.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.empresa.dentalsoft.model.Evolucion;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EvolucionService;

@Controller
@RequestMapping("/evolucion")
public class EvolucionController {

	@Autowired
	private AdministradorService administradorService;
	
	@Autowired
	private EvolucionService evolucionService;
	
	@Autowired
	private CitaService citaService;
	
	List<Evolucion> listEvo = new ArrayList<>();
	
	@GetMapping("/new")
	public String generarEvo(HttpServletRequest request, Model model, @RequestParam("historia_id") Integer historia, 
								@RequestParam("cita_id") Integer cita, @RequestParam("descripcion") String descripcion) {
			
			Evolucion evo = new Evolucion();
			evo.setHistoria_id(historia);
			evo.setCita_id(cita);
			evo.setDescripcion(descripcion);
			evolucionService.save(evo);
		return "";
	}
}
