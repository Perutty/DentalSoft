package co.empresa.dentalsoft.repository;

import org.springframework.data.repository.CrudRepository;

import co.empresa.dentalsoft.model.Hora;

public interface HoraRepository extends CrudRepository<Hora, Integer> {

	public abstract Hora findByHora(String hora);
}
