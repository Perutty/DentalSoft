package co.empresa.dentalsoft.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.AdministradorService;
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
	
	@GetMapping("/list")
	public String list(HttpServletRequest request, Model model){
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		List<Odontologo> odontologos = odontologoService.getAll();
		List<Paciente> pacientes = pacienteService.getAll();
		List<HistoriaClinica> historias = historiaClinicaService.getAll();
		model.addAttribute("odontologos", odontologos);
		model.addAttribute("pacientes", pacientes);
		model.addAttribute("historias", historias);
		model.addAttribute("admin", adm);
		return "historiaclinica";
	}

}
