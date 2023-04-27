package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Sexo;
import co.empresa.dentalsoft.repository.SexoRepository;
import co.empresa.dentalsoft.service.SexoService;

@Service
public class SexoServiceImpl extends GenericServiceImpl<Sexo, Integer> implements SexoService{

	@Autowired
	private SexoRepository sexoRepository;
	
	@Override
	public CrudRepository<Sexo, Integer> getDao(){
		return sexoRepository;
	}
}
