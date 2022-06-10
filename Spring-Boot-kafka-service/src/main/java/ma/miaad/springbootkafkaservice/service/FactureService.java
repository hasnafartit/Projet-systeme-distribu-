package ma.miaad.springbootkafkaservice.service;

import ma.miaad.springbootkafkaservice.Entity.Facture;
import ma.miaad.springbootkafkaservice.repository.FactureRepository;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;
    @Bean
    public Supplier<Facture> factureSupplier(){
        return ()-> new Facture(null
                ,Math.random()>0.5?"C1":"C2",
                new Random().nextInt(9000));
    }

    @Bean
    public Consumer<Facture> factureConsumer(){
        return (input)->{
            System.out.println("*********************");
            Facture facture=new Facture(null,input.getNomClient(),input.getMantant());
            System.out.println(facture.toString());
            factureRepository.save(facture);
            System.out.println("**********************");
        };

    }

    @Bean
    public Function<KStream<String,Facture>
            ,KStream<String,Long>> kStreamFunction(){
        return (input)->{
            return input
                    .filter((k,v)->v.getMantant()>60)
                    .map((k,v)->new KeyValue<>(v.getNomClient(),0L))
                    .groupBy((k,v)->k, Grouped.with(Serdes.String(),Serdes.Long()))
                    .windowedBy(TimeWindows.of(Duration.of(5, ChronoUnit.SECONDS)))
                    .count(Materialized.as("page-count"))
                    .toStream()
                    .map((k,v)->new KeyValue<>("=>"+k.window().startTime()+" "+k.window().endTime()+" : "+k.key(),v));
        };
    }

}
