package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Hora;
import co.empresa.dentalsoft.repository.HoraRepository;
import co.empresa.dentalsoft.service.HoraService;

@Service
public class HoraServiceImpl extends GenericServiceImpl<Hora, Integer> implements HoraService{

	@Autowired
	private HoraRepository horaRepository;
	
	@Override
	public CrudRepository<Hora, Integer> getDao(){
		return horaRepository;
	}
	
	
}
