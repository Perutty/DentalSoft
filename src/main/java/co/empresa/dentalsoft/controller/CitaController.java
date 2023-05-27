package co.empresa.dentalsoft.controller;

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
import co.empresa.dentalsoft.model.Hora;
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.Tratamiento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.HoraService;
import co.empresa.dentalsoft.service.OdontologoService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.service.TratamientoService;

@Controller
@RequestMapping("/cita")
public class CitaController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private OdontologoService odontologoService;
	
	@Autowired
	private TratamientoService tratamientoService;
	
	@Autowired
	private CitaService citaService;
	
	@Autowired
	private HoraService horaService;
	
	@Autowired
	private AdministradorService administradorService;
	
	@GetMapping("/list")
	public String list(HttpServletRequest request, Model model){
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		List<Odontologo> odontologos = odontologoService.getAll();
		List<Paciente> pacientes = pacienteService.getAll();
		List<Tratamiento> tratamientos = tratamientoService.getAll();
		List<Hora> horas = horaService.getAll();
		List<Cita> citas = citaService.getAll();
		model.addAttribute("odontologos", odontologos);
		model.addAttribute("pacientes", pacientes);
		model.addAttribute("tratamientos", tratamientos);
		model.addAttribute("citas", citas);
		model.addAttribute("horas", horas);
		model.addAttribute("admin", adm);
		return "agenda";
	}

	@PostMapping("/save")
	public String save(RedirectAttributes att, Cita cita, HttpServletRequest request, Model model){
				String nombre = (String)request.getSession().getAttribute("nombre_paci");
				cita.setPaciente_doc(nombre);
				citaService.save(cita);
				att.addFlashAttribute("accion", "¡Cita agendada con éxito!");
		return "redirect:/admin/citas/"+nombre;
	}
	
	
	@GetMapping("/delete/{id}")
	public String delete(RedirectAttributes att, @PathVariable("id") String id, Model model){
		citaService.delete(Integer.parseInt(id));
		att.addFlashAttribute("accion", "¡Cita eliminada con éxito!");
		return "redirect:/cita/list";
	}
}
