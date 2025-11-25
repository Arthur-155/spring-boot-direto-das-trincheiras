package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTACE;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> ListAll(@RequestParam(required = false) String nome) {
        var producers = Producer.producers();
        var list = MAPPER.toProducerGetResponseList(producers);
        if (nome == null) return ResponseEntity.ok(list);

        var response = list.stream().filter(p -> p.getName().equalsIgnoreCase(nome)).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        var getProducer = Producer.producers().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst().map(MAPPER::toProducerGetResponse)
                .orElse(null);
        return ResponseEntity.ok(getProducer);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest,
                                                     @RequestHeader HttpHeaders headers) {

        log.info("{}", headers);
        var producer = MAPPER.toProducer(producerPostRequest);
        Producer.producers().add(producer);
        var response = MAPPER.toProducerPostResponse(producer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void>deleteById(@PathVariable Long id){
        var bodyToBeDeleted = Producer.producers().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found"));
        Producer.producers().remove(bodyToBeDeleted);
        return ResponseEntity.noContent().build();
    }
}

