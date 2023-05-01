package co.empresa.dentalsoft;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.empresa.dentalsoft.controller.PacienteController;

@SpringBootApplication
public class DentalsoftApplication {

	public static void main(String[] args) {
		new File(PacienteController.uploadDirectory).mkdir();
		SpringApplication.run(DentalsoftApplication.class, args);
	}

}
