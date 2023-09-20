package co.empresa.dentalsoft.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.Cita;
import co.empresa.dentalsoft.model.Evolucion;
import co.empresa.dentalsoft.model.Hora;
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.Tratamiento;
import co.empresa.dentalsoft.service.AdministradorService;
import co.empresa.dentalsoft.service.CitaService;
import co.empresa.dentalsoft.service.EvolucionService;
import co.empresa.dentalsoft.service.HoraService;
import co.empresa.dentalsoft.service.OdontologoService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.service.TratamientoService;
import co.empresa.dentalsoft.service.impl.EmailService;

@Controller
@RequestMapping("/cita")
public class CitaController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
    private EmailService emailService;
	
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
	
	List<Cita> listBuscarCita = new ArrayList<>();
	
	Set<Hora> horasDisponiblesSet = new HashSet<>();
	
	List<String> horasOcupadas = new ArrayList<>();
	
	ArrayList<String> meses = new ArrayList<String>(Arrays.asList("Enero","Febrero","Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));
	
	
	@GetMapping("/list")
	public String list(HttpServletRequest request, Model model){
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		List<Odontologo> odontologos = odontologoService.getAll();
		List<Paciente> pacientes = pacienteService.getAll();
		List<Tratamiento> tratamientos = tratamientoService.getAll();
		List<Hora> horas = horaService.getAll();
		List<Cita> citas = citaService.getAll();
		citas.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
		model.addAttribute("odontologos", odontologos);
		model.addAttribute("pacientes", pacientes);
		model.addAttribute("tratamientos", tratamientos);
		model.addAttribute("citas", citas);
		model.addAttribute("horas", horas);
		model.addAttribute("admin", adm);
		return "agenda";
	}
	

	@PostMapping("/save")
	public String save(RedirectAttributes att, Cita cita,HttpServletRequest request, Model model){
				String documento = (String)request.getSession().getAttribute("docPaci");
				Paciente p = pacienteService.get(documento);
				Date f = new Date();
				SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
				String fechaString = formatoFecha.format(f);
				String[] fecha = fechaString.split("-");
				String mes = "";
				for(int i = 0; i < meses.size();i++) {
					if(i == Integer.parseInt(fecha[1])-1) {
						mes = meses.get(Integer.parseInt(fecha[1])-1);						
					}
				}
				String fechaCompleta = ""+fecha[0]+" de "+mes+" del año "+fecha[2];
				cita.setPaciente_doc(p.getNombre());
				citaService.save(cita);
				emailService.sendEmail(""+p.getCorreo(), "Recordatorio cita odontológica", "Señor: "+p.getNombre()+"\n\nCordial saludo\n\n\nSu cita "+cita.getTratamiento_cod().toUpperCase()+" ha sido programada para el día "+fechaCompleta+" en el horario de "+cita.getHora()+", con el Dr. "+cita.getOdontologo_doc()+" en el edificio Colegio Médico oficina 402.\n\n\nPor favor, si no puede asistir notifíquenos por nuestros medios de atención.\n\n\n\nGracias.");
				att.addFlashAttribute("accion", "¡Cita agendada con éxito! se envió notificación vía email");
				System.out.println(fechaCompleta);
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
	
	@GetMapping("/horasocupadas/{fecha}/{odontologo}")
	@ResponseBody
	public String horasDisponibles(@PathVariable("fecha") String fecha, @PathVariable("odontologo") String odontologo,Model model){
		List<Cita> citas = citaService.getAll();
		List<Hora> horas = horaService.getAll();
		horasOcupadas.clear();
		citas.forEach((c)->{
			Calendar cal = Calendar.getInstance();
			cal.setTime(c.getFecha());
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);

			String fechaCitaString = String.format("%04d-%02d-%02d", year, month, day);
			if((fechaCitaString.equals(fecha) && c.getOdontologo_doc().equals(odontologo))) {
				horas.forEach((h) -> {
					if(h.getHora().equals(c.getHora()))
						horasOcupadas.add(h.getHora());
				});
			}
		});
		Collections.sort(horasOcupadas);
		return horasOcupadas.toString();
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
	public String buscarCitas(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha, 
								@RequestParam("estado") String estado, HttpServletRequest request, 
								RedirectAttributes att,Model model) {
		
		Administrador adm = administradorService.get((String)request.getSession().getAttribute("admin_doc"));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Cita> citas = citaService.getAll();
		listBuscarCita.clear();
		citas.forEach((cita) -> {
			String fechaCita = dateFormat.format(cita.getFecha());
			String fechaBuscar = dateFormat.format(fecha);
		if(fechaCita.equals(fechaBuscar) && cita.getEstado().equals(estado)) {
				listBuscarCita.add(cita);
				listBuscarCita.sort(Comparator.comparing(Cita::getHora));	
				model.addAttribute("fecha", fechaBuscar);
				model.addAttribute("estado", estado);
		}else
		{
			model.addAttribute("fecha", fechaBuscar);
			model.addAttribute("estado", estado);
		}
		});
		model.addAttribute("citas", listBuscarCita);
		model.addAttribute("admin", adm);
		return "agenda";
	}
	
	
	
}
