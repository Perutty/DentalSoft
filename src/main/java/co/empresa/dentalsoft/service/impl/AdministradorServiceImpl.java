package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.repository.AdministradorRepository;
import co.empresa.dentalsoft.service.AdministradorService;

@Service
public class AdministradorServiceImpl extends GenericServiceImpl<Administrador, Integer> implements AdministradorService {
	
	@Autowired
	public AdministradorRepository administradorRepository;
	
	@Override
	public CrudRepository<Administrador, Integer> getDao(){
		return administradorRepository;
	}
	
	@Override
	public Administrador select(String documento, String password) {
		return administradorRepository.findByDocumentoAndPassword(documento, password);
	}
	

}
