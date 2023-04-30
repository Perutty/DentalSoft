package co.empresa.dentalsoft.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table (name="administrador")
@Data
public class Administrador {
	
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
	
	public Administrador(){};
	
	public Administrador(String documento, String password) {
		this.documento = documento;
		this.password = password;
	}

}
