package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.HistoriaClinica;
import co.empresa.dentalsoft.repository.HistoriaClinicaRepository;
import co.empresa.dentalsoft.service.HistoriaClinicaService;

@Service
public class HistoriaClinicaServiceImpl extends GenericServiceImpl<HistoriaClinica, Integer> implements HistoriaClinicaService{
	
	@Autowired
	private HistoriaClinicaRepository historiaClinicaRepository;
	
	@Override
	public CrudRepository<HistoriaClinica, Integer> getDao(){
		return historiaClinicaRepository;
	}
}
