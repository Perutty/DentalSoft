package co.empresa.dentalsoft.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.empresa.dentalsoft.model.Eps;
import co.empresa.dentalsoft.model.EstadoCivil;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.model.Pais;
import co.empresa.dentalsoft.model.Sexo;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.service.EpsService;
import co.empresa.dentalsoft.service.EstadoCivilService;
import co.empresa.dentalsoft.service.PacienteService;
import co.empresa.dentalsoft.service.PaisService;
import co.empresa.dentalsoft.service.SexoService;
import co.empresa.dentalsoft.service.TipoDocumentoService;

@Controller
@RequestMapping("/paciente")
public class PacienteController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private EpsService epsService;
	
	@Autowired
	private EstadoCivilService estadoCivilService;
	
	@Autowired
	private SexoService sexoService;
	
	@Autowired
	private TipoDocumentoService tipoDocumentoService;
	
	
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpSession session, Model model) {
		if(request.getSession().getAttribute("paciente_doc") != null) {
			return "redirect:/paciente/dashboard";
		}else
			return "login";
	}
	
	@PostMapping("/save")
	public String save(RedirectAttributes att, Paciente paciente, Model model) {
		pacienteService.save(paciente);
		att.addFlashAttribute("accion", "¡Paciente registrado con éxito!");
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/edit/{documento}")
	public String edit(RedirectAttributes att, HttpServletRequest request, @PathVariable("documento") String documento, Model model){
		String adm_nombre = (String)request.getSession().getAttribute("admin");
		List<TipoDocumento> tipoDoc = tipoDocumentoService.getAll();
		List<EstadoCivil> estadoCivil = estadoCivilService.getAll();
		List<Paciente> paciente = pacienteService.getAll();
		List<Pais> pais = paisService.getAll();
		List<Sexo> sexo = sexoService.getAll();
		List<Eps> eps = epsService.getAll();
		model.addAttribute("tipoDoc", tipoDoc);
		model.addAttribute("estadocivil", estadoCivil);
		model.addAttribute("paciente", paciente);
		model.addAttribute("eps", eps);
		model.addAttribute("sexo", sexo);
		model.addAttribute("pais", pais);
		model.addAttribute("admin", adm_nombre);
		model.addAttribute("paciente", pacienteService.get(documento));
		return "editpaciente";
	}
	
	@GetMapping("/delete/{documento}")
	public String delete(RedirectAttributes att, @PathVariable("documento") String documento, Model model){
		pacienteService.delete(documento);
		att.addFlashAttribute("accion", "¡Paciente eliminado con éxito!");
		return "redirect:/admin/dashboard";
	}

}
