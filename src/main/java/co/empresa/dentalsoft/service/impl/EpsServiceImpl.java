package co.empresa.dentalsoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.Eps;
import co.empresa.dentalsoft.repository.EpsRepository;
import co.empresa.dentalsoft.service.EpsService;

@Service
public class EpsServiceImpl extends GenericServiceImpl<Eps, Integer> implements EpsService{

	@Autowired
	private EpsRepository epsRepository;
	
	@Autowired
	public CrudRepository<Eps, Integer> getDao(){
		return epsRepository;
	}
}
