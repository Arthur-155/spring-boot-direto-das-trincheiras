package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {

    @GetMapping
    public List<Producer> ListAll(@RequestParam(required = false) String nome) {
        var producers = Producer.producers;
        if(nome == null) return producers;

        return producers.stream().filter(producer -> producer.getName().equalsIgnoreCase(nome)).toList();
    }

    @GetMapping("{id}")
    public Producer findById(@PathVariable Long id) {
        return Producer.producers.stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst().orElse(null);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key")
    public ResponseEntity<Producer> save(@RequestBody Producer producer,
                                         @RequestHeader HttpHeaders headers){

        log.info("{}", headers);
        producer.setId(ThreadLocalRandom.current().nextLong(1000));
        Producer.producers.add(producer);
        var responseHeaders = new HttpHeaders();
        responseHeaders.add("Authorization","Minha Chave");
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(producer);

    }
}

