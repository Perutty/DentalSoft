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
	public String generarEvo(RedirectAttributes att, HttpServletRequest request, Model model, @PathVariable("idCita") Integer idCita,  @PathVariable("idHistoria") Integer idHistoria) {
			Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
			List<Evolucion> evos = evolucionService.getAll();
			listEvo.clear();
			evos.forEach((e)->{
				if(e.getCita_id().equals(idCita) && e.getHistoria_id().equals(idHistoria)) {
					listEvo.add(e);
				}
			});
			Cita cita = citaService.get(idCita);
			HistoriaClinica hc = historiaClinicaService.get(idHistoria);
			if(listEvo.isEmpty()) {
			model.addAttribute("paci", hc.getPaciente_doc());
			model.addAttribute("cita", cita);
			model.addAttribute("historia", hc);
			model.addAttribute("admin", adm);
				return "evolucion";
			}else {
				att.addFlashAttribute("accion", "¡Esta cita ya cuenta con una evolución!");
				return "redirect:/admin/citas/"+hc.getPaciente_doc();
			}
	}
	
	
	@PostMapping("/save")
	public String save(RedirectAttributes att, Model model, @RequestParam  String historia_id, @RequestParam String cita_id, 
						@RequestParam String descripcion) {
		Evolucion evo = new Evolucion();
		String documento = historiaClinicaService.get(Integer.valueOf(historia_id)).getPaciente_doc();
		evo.setCita_id(Integer.valueOf(cita_id));
		evo.setHistoria_id(Integer.valueOf(historia_id));
		evo.setDescripcion(descripcion);
		evolucionService.save(evo);
		att.addFlashAttribute("accion", "¡Evolución agregada con éxito!");
		return "redirect:/admin/citas/"+documento;
	}
}
