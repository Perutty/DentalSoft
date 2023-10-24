package co.empresa.dentalsoft.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.DocumentException;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.Cita;
import co.empresa.dentalsoft.model.Evolucion;
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EvolucionService;
import co.empresa.dentalsoft.service.HistoriaClinicaService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.util.HistoriaClinicaExport;

@Controller
@RequestMapping("/historiaclinica")
public class HistoriaClinicaController {
	
	@Autowired
	private PacienteService pacienteService;
	
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
		
		List<HistoriaClinica> historias = historiaClinicaService.getAll();
		List<Cita> citas = citaService.getAll();
		List<Evolucion> evos = evolucionService.getAll();
		ev.clear();
		cita.clear();
		historias.forEach((historia)->{
			if(historia.getPaciente_doc().equals(documento)){
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
		model.addAttribute("nombre", pacienteService.get(documento).getNombre());
		model.addAttribute("paci", pacienteService.get(documento));
		model.addAttribute("admin", administradorService.get((String)request.getSession().getAttribute("admin_doc")));
		return "historiaclinica";
	}
	
	@GetMapping("/exportarpdf/{documento}")
	public void exportarPDF(HttpServletResponse response,RedirectAttributes att, 
								@PathVariable("documento") String documento) throws DocumentException, IOException {
		
		List<HistoriaClinica> historias = historiaClinicaService.getAll();
		List<Cita> citas = citaService.getAll();
		List<Evolucion> evos = evolucionService.getAll();
		ev.clear();
		cita.clear();
		historias.forEach((historia)->{
			if(historia.getPaciente_doc().equals(documento)){
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
			response.setContentType("application/pdf");
			String cabecera = "Content-Disposition";
			String valor = "attachment; filename=HC_" + pacienteService.get(documento).getNombre() + ".pdf";
			response.setHeader(cabecera, valor);
			
			HistoriaClinicaExport hc = new HistoriaClinicaExport(ev, cita, pacienteService.get(documento).getNombre());
			hc.exportar(response);
		
	}
}
