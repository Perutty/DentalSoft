package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Administrador;
import co.empresa.dentalsoft.model.Odontologo;
import co.empresa.dentalsoft.repository.OdontologoRepository;
import co.empresa.dentalsoft.service.OdontologoService;

@Service
public class OdontologoServiceImpl extends GenericServiceImpl<Odontologo, String> implements OdontologoService{
	
	@Autowired
	private OdontologoRepository odontologoRepository;
	
	@Override
	public CrudRepository<Odontologo, String> getDao(){
		return odontologoRepository;
	}
	
	@Override
	public Odontologo select(String documento, String password) {
		return odontologoRepository.findByDocumentoAndPassword(documento, password);
	}
}
