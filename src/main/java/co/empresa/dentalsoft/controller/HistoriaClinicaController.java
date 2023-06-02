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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.Cita;
import co.empresa.dentalsoft.model.Evolucion;
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EvolucionService;
import co.empresa.dentalsoft.service.HistoriaClinicaService;
import co.empresa.dentalsoft.service.OdontologoService;
import co.empresa.dentalsoft.service.PacienteService;

@Controller
@RequestMapping("/historiaclinica")
public class HistoriaClinicaController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private OdontologoService odontologoService;
	
	@Autowired
	private AdministradorService administradorService;
	
	@Autowired
	private HistoriaClinicaService historiaClinicaService;
	
	@Autowired
	private EvolucionService evolucionService;
	
	@Autowired
	private CitaService citaService;
	
	public List<Evolucion> ev = new ArrayList<>();
	
	public List<Cita> cita = new ArrayList<>();
	
	@GetMapping("/ver/{documento}")
	public String list(HttpServletRequest request,  @PathVariable("documento") String documento, Model model){
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		List<Odontologo> odontologos = odontologoService.getAll();
		Paciente paci = pacienteService.get(documento);
		List<HistoriaClinica> historias = historiaClinicaService.getAll();
		List<Cita> citas = citaService.getAll();
		List<Evolucion> evos = evolucionService.getAll();
		ev.clear();
		cita.clear();
		historias.forEach((historia)->{
			if(historia.getPaciente_doc().equals(paci.getDocumento())){
				evos.forEach((e)->{
					if(e.getHistoria_id().equals(historia.getId()))
					{
						ev.add(e);
						citas.forEach((c)->{
							if(c.getId().equals(e.getCita_id())) {
								cita.add(c);
							}
						});
					}
				});
			}
		});
		model.addAttribute("cita", cita);
		model.addAttribute("evos", ev);
		model.addAttribute("nombre", paci.getNombre());
		model.addAttribute("odontologos", odontologos);
		model.addAttribute("admin", adm);
		return "historiaclinica";
	}
}
