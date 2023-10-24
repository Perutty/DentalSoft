package co.empresa.dentalsoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import co.empresa.dentalsoft.commands.GenericServiceImpl;
import co.empresa.dentalsoft.model.TipoDocumento;
import co.empresa.dentalsoft.repository.TipoDocumentoRepository;
import co.empresa.dentalsoft.service.TipoDocumentoService;


@Service
public class TipoDocumentoServiceImpl extends GenericServiceImpl<TipoDocumento, Integer> implements TipoDocumentoService{

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;
	
	@Override
	public CrudRepository<TipoDocumento, Integer> getDao(){
		return tipoDocumentoRepository;
	}
}
