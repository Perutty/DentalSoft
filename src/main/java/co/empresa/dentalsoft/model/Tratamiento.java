package co.empresa.dentalsoft.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table (name="tratamiento")
@Data
public class Tratamiento {

	@Id
	@Column
	private String codigo;
	
	@Column
	private String descripción;
	
	public Tratamiento(){};
}
