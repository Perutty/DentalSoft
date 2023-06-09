package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Paciente;
import co.empresa.dentalsoft.repository.PacienteRepository;
import co.empresa.dentalsoft.service.PacienteService;

@Service
public class PacienteServiceImpl extends GenericServiceImpl<Paciente, String> implements PacienteService{
	
	@Autowired
	public PacienteRepository pacienteRepository;
	
	@Override
	public CrudRepository <Paciente, String> getDao(){
		return pacienteRepository;
	}
	
	@Override
	public Paciente select(String documento, String password) {
		return pacienteRepository.findByDocumentoAndPassword(documento, password);
	}
	
	@Override
	public Paciente search(String nombre) {
		return pacienteRepository.findByNombre(nombre);
	}
}
