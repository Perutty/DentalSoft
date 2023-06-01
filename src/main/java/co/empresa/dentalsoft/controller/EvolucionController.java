package co.empresa.dentalsoft.controller;

import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.Cita;
import co.empresa.dentalsoft.model.Evolucion;
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EvolucionService;
import co.empresa.dentalsoft.service.HistoriaClinicaService;

@Controller
@RequestMapping("/evolucion")
public class EvolucionController {

	@Autowired
	private AdministradorService administradorService;
	
	@Autowired
	private EvolucionService evolucionService;
	
	@Autowired
	private CitaService citaService;
	
	@Autowired
	private HistoriaClinicaService historiaClinicaService;
	
	List<Evolucion> listEvo = new ArrayList<>();
	
	@GetMapping("/new/{idCita}/{idHistoria}")
	public String generarEvo(HttpServletRequest request, Model model, @PathVariable("idCita") Integer idCita,  @PathVariable("idHistoria") Integer idHistoria) {
			Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
			Cita cita = citaService.get(idCita);
			HistoriaClinica hc = historiaClinicaService.get(idHistoria);
			model.addAttribute("cita", cita);
			model.addAttribute("historia", hc);
			model.addAttribute("admin", adm);
		return "evolucion";
	}
	
	@GetMapping("/ver")
	public String ver(HttpServletRequest request, Model model) {
		return "";
	}
	
	@PostMapping("/save")
	public String save(Model model, @RequestParam  Integer historia_id, @RequestParam Integer cita_id, 
						@RequestParam String descripcion, RedirectAttributes att) {
		Evolucion evo = new Evolucion();
		evo.setCita_id(cita_id);
		evo.setHistoria_id(historia_id);
		evo.setDescripcion(descripcion);
		evolucionService.save(evo);
		return "redirect:/citas/ver";
	}
}
