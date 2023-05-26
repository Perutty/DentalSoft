package co.empresa.dentalsoft.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table (name="cita")
@Data
public class Cita {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String paciente_doc;
	
	@Column
	private String odontologo_doc;
	
	@Column
	private String tratamiento_cod;
	
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;

	public Cita() {};
}
