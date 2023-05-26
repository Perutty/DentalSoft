package co.empresa.dentalsoft.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table (name="odontologo")
@Data
public class Odontologo {
	
	@Column
	private String nombre;
	
	@Column
	private String correo;
	
	@Column
	private String tipodoc;
	
	@Id
	@Column
	private String documento;
	
	@Column
	private String celular;
	
	@Column
	private String password;
	
	@Column
	private String foto;
	
	public Odontologo() {};
}
