package co.empresa.dentalsoft.repository;

import org.springframework.data.repository.CrudRepository;

import co.empresa.dentalsoft.model.Administrador;

public interface AdministradorRepository extends CrudRepository<Administrador, String> {

	public abstract Administrador findByDocumentoAndPassword(String documento, String password);
}
