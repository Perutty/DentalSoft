package co.empresa.dentalsoft.service;

import co.empresa.dentalsoft.commands.GenericService;
import co.empresa.dentalsoft.model.Administrador;

public interface AdministradorService extends GenericService<Administrador, String>{

	public Administrador select(String documento, String password);
}
