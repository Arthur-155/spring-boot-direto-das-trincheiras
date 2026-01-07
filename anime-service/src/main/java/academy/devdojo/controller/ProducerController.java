package academy.devdojo.controller;

import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import academy.devdojo.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("v1/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerMapper mapper;

    private final ProducerService service;


    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String nome) {
        var producers = service.findAll(nome);
        var getResponses = mapper.toProducerGetResponseList(producers);
        return ResponseEntity.status(HttpStatus.OK).body(getResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        var getProducerBody = service.findByIdOrThrowNotFound(id);
        var getProducerResponse = mapper.toProducerGetResponse(getProducerBody);
        return ResponseEntity.ok(getProducerResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest,
                                                     @RequestHeader HttpHeaders headers) {

        log.info("{}", headers);
        var producer = mapper.toProducer(producerPostRequest);
        var productSaved = service.save(producer);
        var response = mapper.toProducerPostResponse(productSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void>deleteById(@PathVariable Long id){
        service.deleteByIdOrThrowNotFound(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void>updateProduct(@RequestBody ProducerPutRequest request){
        var producerUpdated = mapper.toPutProducer(request);
        service.updateOrThrowNotFound(producerUpdated);
        return ResponseEntity.noContent().build();
    }
}

