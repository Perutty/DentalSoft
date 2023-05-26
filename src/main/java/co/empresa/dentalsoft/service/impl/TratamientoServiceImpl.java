package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Tratamiento;
import co.empresa.dentalsoft.repository.TratamientoRepository;
import co.empresa.dentalsoft.service.TratamientoService;

@Service
public class TratamientoServiceImpl extends GenericServiceImpl <Tratamiento, String> implements TratamientoService{

	@Autowired
	public TratamientoRepository tratamientoRepository;
	
	@Override
	public CrudRepository<Tratamiento, String> getDao(){
		return tratamientoRepository;
	}
	
	
}
