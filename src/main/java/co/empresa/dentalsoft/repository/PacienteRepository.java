package co.empresa.dentalsoft.repository;

import org.springframework.data.repository.CrudRepository;

import co.empresa.dentalsoft.model.Paciente;

public interface PacienteRepository extends CrudRepository<Paciente, String> {

	public abstract Paciente findByDocumentoAndPassword(String documento, String password);
	
	public abstract Paciente findByNombre(String nombre);
}
