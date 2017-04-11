package org.dmr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
@SpringBootApplication
public class CallingToInternetApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CallingToInternetApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CallingToInternetApplication.class);
    }
	
}
