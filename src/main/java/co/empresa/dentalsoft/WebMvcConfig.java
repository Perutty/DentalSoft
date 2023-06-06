package co.empresa.dentalsoft;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import co.empresa.dentalsoft.controller.AdministradorController;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/fotos/**").addResourceLocations(System.getProperty("user.home")+"/dentalsoft/");
	}
	
	
	
	
	
	
}
