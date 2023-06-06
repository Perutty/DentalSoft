package co.empresa.dentalsoft;


import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.empresa.dentalsoft.controller.AdministradorController;


@SpringBootApplication
public class DentalsoftApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(DentalsoftApplication.class, args);
	}

}
