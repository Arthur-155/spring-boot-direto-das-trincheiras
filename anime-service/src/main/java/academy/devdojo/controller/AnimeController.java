package academy.devdojo.controller;

import academy.devdojo.exception.DefaultErrorMessage;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.mapper.AnimesMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import academy.devdojo.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/animes")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimesMapper mapper;
    private final AnimeService service;


    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> ListAll(@RequestParam(required = false) String nome) {
        var animes = service.listAll(nome);
        var animeGetResponseList = mapper.toAnimeGetResponseList(animes);
        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponseList);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        var getAnimeByIdResponse = service.findByIdOrThrowNotFound(id);
        var response = mapper.toAnimeGetResponse(getAnimeByIdResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> createAnime(@RequestBody @Valid AnimePostRequest animePostRequest) {

        log.info("{}", animePostRequest);
        var request = mapper.toAnime(animePostRequest);
        var saved = service.save(request);
        var response = mapper.toAnimePostResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteOrThrowNotFound(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        var animeUpdated = mapper.toPutAnime(request);
        service.updateOrThrowNotFound(animeUpdated);
        return ResponseEntity.noContent().build();
    }


}

