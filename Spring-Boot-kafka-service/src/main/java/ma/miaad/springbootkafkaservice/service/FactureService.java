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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
            Facture facture=new Facture(null,input.getNomClient(),input.getMantant());
            factureRepository.save(facture);
            System.out.println("**********************");
            System.out.println("**********************");

            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new File("Factures.csv"));
            } catch (
                    FileNotFoundException e) {
                e.printStackTrace();
            }
            StringBuilder builder = new StringBuilder();
            String ColumnNamesList = "Id,NameClient,Mantant";
            builder.append(ColumnNamesList +"\n");
            factureRepository.findAll().forEach(f ->{
                builder.append(f.getId().toString()+",");
                builder.append(f.getNomClient().toString()+",");
                builder.append(f.getMantant());
                builder.append('\n');
                System.out.println(f.toString());
                System.out.println("--------------------");
            });
            pw.write(builder.toString());
            pw.close();
        };
    }



    @Bean
    public  Function<KStream<String,Facture>
            ,KStream<String,Long>> kStreamFunction(){
        return (input)->{
            return input

                    .map((k,v)->new KeyValue<>(v.getNomClient(),0L))
                    .groupBy((k,v)->k,Grouped.with(Serdes.String(),Serdes.Long()))
                    .windowedBy(TimeWindows.of(Duration.of(5, ChronoUnit.SECONDS)))
                    .count(Materialized.as("page-count"))
                    .toStream()
                    .map((k,v)->new KeyValue<>("=>"+k.window().startTime()+" "+k.window().endTime()+" : "+k.key(),v));
        };
    }

}
