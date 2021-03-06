package ma.miaad.springbootkafkaservice.web;

import ma.miaad.springbootkafkaservice.Entity.Facture;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class FactureRestController {
    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private InteractiveQueryService interactiveQueryService;

    @GetMapping("/publish/{topic}")
    public Facture publish(@PathVariable String topic){
        Facture facture= new Facture(null,Math.random()>0.5?"C1":"C2",new Random().nextInt(9000));
        streamBridge.send(topic,facture);
        return facture;
    }


    @GetMapping(path = "/analytics",produces = MediaType.TEXT_EVENT_STREAM_VALUE)

    // @GetMapping(path = "/analytics/{page}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)

    public Flux<Map<String, Long>>  analytics(/*@PathVariable String page*/){

        return Flux.interval(Duration.ofSeconds(1))

                .map(sequence->{
                    Map<String,Long> stringLongMap=new HashMap<>();
                    ReadOnlyWindowStore<String, Long> windowStore = interactiveQueryService.getQueryableStore("page-count", QueryableStoreTypes.windowStore());

                    Instant now=Instant.now();
                    Instant from=now.minusMillis(5000);

                    KeyValueIterator<Windowed<String>, Long> fetchAll = windowStore.fetchAll(from, now);
                    //WindowStoreIterator<Long> fetchAll = windowStore.fetch(page, from, now);

                    while (fetchAll.hasNext()){
                        KeyValue<Windowed<String>, Long> next = fetchAll.next();
                        // KeyValue<Long,Long> next = fetchAll.next();
                        // stringLongMap.put(page,next.value);
                        stringLongMap.put(next.key.key(),next.value);
                    }

                    return stringLongMap;
                }).share();
    }


}


