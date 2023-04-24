package co.empresa.dentalsoft.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Data;

@Entity
@Table (name="paciente")
@Data
public class Paciente {
	
	@Column
	private Integer tipodoc_id;
	
	@Id
	@Column
	private String documento;
	
	@Column
	private String nombre;
	
	@Column
	private Integer sexo_id;
	
	@Column
	private Integer estadocivil_id;
	
	@Column
	private Integer paisnac_id;
	
	@Column
	private String ciudadnac;
	
	@Column
	@DateTimeFormat(iso = ISO.DATE)
	private Date fechanac;
	
	@Column
	@DateTimeFormat(iso = ISO.DATE)
	private Date fechaingreso;
	
	@Column
	private Integer paisdomi_id;
	
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
	private Integer eps_id;
	
	@Column
	private String tipoafiliacion;
	
	@Column
	private String poliza;
	
	@Column
	private byte[] foto;
	
	@Column
	private String password;
	
	public Paciente(){};
	
	public Paciente (String documento, String password) {
		this.documento = documento;
		this.password = password;
	}
}
