package org.miaad.customerservice;

import org.miaad.customerservice.entities.Customer;
import org.miaad.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository, RepositoryRestConfiguration restConfiguration){
		return args -> {
			restConfiguration.exposeIdsFor(Customer.class);
			customerRepository.save(new Customer(null,"Mohamed","med@gmail.com"));
			customerRepository.save(new Customer(null,"HASSAN","mhassan@gmail.com"));
			customerRepository.save(new Customer(null,"SALIMA","salima@gmail.com"));

			customerRepository.findAll().forEach(c ->{
				System.out.println(c.toString());
			});
		};
	}

}
