package co.empresa.dentalsoft.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table (name="paciente")
@Data
public class Paciente {
	
	@Column
	private String tipodoc;
	
	@Id
	@Column
	private String documento;
	
	@Column
	private String nombre;
	
	@Column
	private String sexo;
	
	@Column
	private String estadocivil;
	
	@Column
	private String paisnac;
	
	@Column
	private String ciudadnac;
	
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechanac;
	
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaingreso;
	
	@Column
	private String paisdomi;
	
	@Column
	private String ciudaddomi;
	
	@Column
	private String direcciondomi;
	
	@Column
	private String barriodomi;
	
	@Column
	private String celular;
	
	@Column
	private String correo;
	
	@Column
	private String ocupacion;
	
	@Column
	private String eps;
	
	@Column
	private String tipoafiliacion;
	
	@Column
	private String poliza;
	
	@Column
	private String foto;
	
	@Column
	private String password;
	
	public Paciente(){};
	
	public Paciente (String documento, String password) {
		this.documento = documento;
		this.password = password;
	}
}
