package co.empresa.dentalsoft.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
	
	List<Cita> listCitas = new ArrayList<>();

	List<Cita> citasHistorial = new ArrayList<>();
	
	
	@GetMapping("/list")
	public String list(HttpServletRequest request, Model model){
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		List<Odontologo> odontologos = odontologoService.getAll();
		List<Paciente> pacientes = pacienteService.getAll();
		List<Tratamiento> tratamientos = tratamientoService.getAll();
		List<Hora> horas = horaService.getAll();
		List<Cita> citas = citaService.getAll();
		listCitas.clear();
		citas.forEach((cita)->{
			if(cita.getEstado().equals("Pendiente"))
				listCitas.add(cita);
		});
		listCitas.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
		model.addAttribute("odontologos", odontologos);
		model.addAttribute("pacientes", pacientes);
		model.addAttribute("tratamientos", tratamientos);
		model.addAttribute("citas", listCitas);
		model.addAttribute("horas", horas);
		model.addAttribute("admin", adm);
		return "agenda";
	}
	
	@GetMapping("/historial")
	public String citasHistorial(HttpServletRequest request, Model model){
		
			Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
			
			List<Cita> listCitas = citaService.getAll();
			List<Tratamiento> tratamientos = tratamientoService.getAll();
			List<Odontologo> odontologos = odontologoService.getAll();
			List<Hora> horas = horaService.getAll();
			citasHistorial.clear();
			listCitas.forEach((cita)->{
				if(cita.getEstado().equals("Finalizada"))
					citasHistorial.add(cita);
			});
			citasHistorial.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
			model.addAttribute("tratamientos", tratamientos);
			model.addAttribute("horas", horas);
			model.addAttribute("odontologos", odontologos);
			model.addAttribute("citas", citasHistorial);
			model.addAttribute("admin", adm);
			return "agenda";
	}
	

	@PostMapping("/save")
	public String save(RedirectAttributes att, Cita cita, HttpServletRequest request, Model model){
				String documento = (String)request.getSession().getAttribute("docPaci");
				Paciente p = pacienteService.get(documento);
				cita.setPaciente_doc(p.getNombre());
				citaService.save(cita);
				att.addFlashAttribute("accion", "¡Cita agendada con éxito!");
				return "redirect:/admin/citas/"+documento;
	}
	
	
	@GetMapping("/delete/{id}")
	public String delete(RedirectAttributes att, HttpServletRequest request, @PathVariable("id") String id, Model model){
		String documento = (String)request.getSession().getAttribute("docPaci");
		citaService.delete(Integer.parseInt(id));
		att.addFlashAttribute("accion", "¡Cita eliminada con éxito!");
		return "redirect:/admin/citas/"+documento;
	}
	
	@GetMapping("/editCita/{id}")
	public String editar(RedirectAttributes att, @PathVariable("id") String id, HttpServletRequest request, Model model) {
		if(id!=null) {
			Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
			String docPaci = (String)request.getSession().getAttribute("docPaci");
			Paciente paci = pacienteService.get(docPaci);
			List<Odontologo> odontologos = odontologoService.getAll();
			List<Tratamiento> tratamientos = tratamientoService.getAll();
			List<Hora> horas = horaService.getAll();
			Cita c = citaService.get(Integer.parseInt(id));
			model.addAttribute("nombre", paci.getNombre());
			model.addAttribute("paci", paci);
			model.addAttribute("cita", c);
			model.addAttribute("odontologos", odontologos);
			model.addAttribute("tratamientos", tratamientos);
			model.addAttribute("horas", horas);
			model.addAttribute("adm", adm);
		}else {
			model.addAttribute("cita", new Cita());
		}
		return "editarcita";
	}
	
	@GetMapping("/atender/{id}")
	public String atenderCita(RedirectAttributes att, @PathVariable("id") String id, HttpServletRequest request, Model model) {
		String documento = (String)request.getSession().getAttribute("docPaci");
		Paciente p = pacienteService.get(documento);
		Cita cita = citaService.get(Integer.parseInt(id));
		cita.setEstado("Finalizada");
		citaService.save(cita);
		att.addFlashAttribute("accion", "¡Cita finalizada con éxito!");
		return "redirect:/admin/citas/"+p.getDocumento();
	}
	
	@GetMapping("/buscar")
	public String buscarCitas(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha, HttpServletRequest request, Model model) {
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Cita> citas = citaService.getAll();
		listCitas.clear();
		citas.forEach((cita) -> {
			String fechaCita = dateFormat.format(cita.getFecha());
			String fechaBuscar = dateFormat.format(fecha);
		if(fechaCita.equals(fechaBuscar))
				listCitas.add(cita);
		System.out.println(fecha);
		});
		model.addAttribute("citas", listCitas);	
		model.addAttribute("admin", adm);
		return "agenda";
	}
	
	
	
}
