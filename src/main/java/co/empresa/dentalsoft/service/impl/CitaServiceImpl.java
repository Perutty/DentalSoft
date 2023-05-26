package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Cita;
import co.empresa.dentalsoft.repository.CitaRepository;
import co.empresa.dentalsoft.service.CitaService;

@Service
public class CitaServiceImpl extends GenericServiceImpl<Cita, Integer> implements CitaService{

	@Autowired
	private CitaRepository citaRepository;
	
	@Override
	public CrudRepository<Cita, Integer> getDao(){
		return citaRepository;
	}
}
