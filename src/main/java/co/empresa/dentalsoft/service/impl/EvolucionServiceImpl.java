package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Evolucion;
import co.empresa.dentalsoft.repository.EvolucionRepository;
import co.empresa.dentalsoft.service.EvolucionService;

@Service
public class EvolucionServiceImpl extends GenericServiceImpl<Evolucion,Integer> implements EvolucionService{
	
	@Autowired
	private EvolucionRepository evolucionRepository;
	
	@Override
	public CrudRepository<Evolucion, Integer> getDao(){
		return evolucionRepository;
	}
}
