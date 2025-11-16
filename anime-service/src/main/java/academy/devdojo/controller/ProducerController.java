package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/producers")
public class ProducerController {

    @GetMapping
    public List<Producer> ListAll(@RequestParam(required = false) String nome) {
        var producers = Producer.Producer;
        if(nome == null) return producers;

        return producers.stream().filter(producer -> producer.getName().equalsIgnoreCase(nome)).toList();
    }

    @GetMapping("{id}")
    public Producer findById(@PathVariable Long id) {
        return Producer.Producer.stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst().orElse(null);
    }

    @PostMapping
    public Producer createProducer(@RequestBody Producer producer){
        Long id = ThreadLocalRandom.current().nextLong(1,1000);
        producer.setId(id);
        Producer.Producer.add(producer);
        return producer;
    }
}

