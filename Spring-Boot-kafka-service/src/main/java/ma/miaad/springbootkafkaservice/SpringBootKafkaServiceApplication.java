package ma.miaad.springbootkafkaservice;

import ma.miaad.springbootkafkaservice.Entity.Facture;
import ma.miaad.springbootkafkaservice.repository.FactureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class SpringBootKafkaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKafkaServiceApplication.class, args);
    }
/*
    @Bean
    CommandLineRunner start(FactureRepository factureRepository, RepositoryRestConfiguration restConfiguration){
        return args -> {
            restConfiguration.exposeIdsFor(Facture.class);
            factureRepository.save(new Facture(null,"ahmed",1L));

            factureRepository.findAll().forEach(c ->{
                System.out.println(c.toString());
            });
        };
    }
*/
}
