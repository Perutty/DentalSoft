package co.empresa.dentalsoft.service;

import co.empresa.dentalsoft.commands.GenericService;
import co.empresa.dentalsoft.model.Paciente;

public interface PacienteService extends GenericService<Paciente, String>{
	
	public Paciente select(String documento, String password);
	
	public Paciente search(String nombre);
	
}
