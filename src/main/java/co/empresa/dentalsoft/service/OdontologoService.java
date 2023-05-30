package co.empresa.dentalsoft.service;

import co.empresa.dentalsoft.commands.GenericService;
import co.empresa.dentalsoft.model.Odontologo;

public interface OdontologoService extends GenericService<Odontologo, String>{

	public Odontologo select(String documento, String password);
}
