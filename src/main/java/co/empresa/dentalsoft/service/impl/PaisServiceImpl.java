package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Pais;
import co.empresa.dentalsoft.repository.PaisRepository;
import co.empresa.dentalsoft.service.PaisService;

@Service
public class PaisServiceImpl extends GenericServiceImpl<Pais, Integer> implements PaisService{

	@Autowired
	private PaisRepository paisRepository;
	
	@Override
	public CrudRepository<Pais, Integer> getDao(){
		return paisRepository;
	}
}
