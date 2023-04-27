package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.EstadoCivil;
import co.empresa.dentalsoft.repository.EstadoCivilRepository;
import co.empresa.dentalsoft.service.EstadoCivilService;

@Service
public class EstadoCivilServiceImpl extends GenericServiceImpl<EstadoCivil, Integer> implements EstadoCivilService{

	@Autowired
	private EstadoCivilRepository estadoCivilRepository;
	
	@Override
	public CrudRepository<EstadoCivil, Integer> getDao(){
		return estadoCivilRepository;
	}
}
