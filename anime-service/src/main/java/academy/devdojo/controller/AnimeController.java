package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.mapper.AnimesMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/animes")
public class AnimeController {

    private static final AnimesMapper MAPPER = AnimesMapper.INSTANCE;

    @GetMapping
    public ResponseEntity <List<AnimeGetResponse>> ListAll(@RequestParam(required = false) String nome) {

        var animes = Anime.getAnimes();
        var animeGetResponseList = MAPPER.toAnimeGetResponseList(animes);
        if(nome == null) return ResponseEntity.ok(animeGetResponseList);

        var response = animeGetResponseList.stream().filter(anime -> anime.getName().equalsIgnoreCase(nome)).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        var getAnimeReponse =  Anime.getAnimes().stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .map(MAPPER::toAnimeGetResponse)
                .orElse(null);
        return ResponseEntity.ok(getAnimeReponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> createAnime(@RequestBody AnimePostRequest animePostRequest){

        log.info("{}",animePostRequest);
        var request = MAPPER.toAnime(animePostRequest);
        var response = MAPPER.toAnimePostResponse(request);

        Anime.getAnimes().add(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
        public ResponseEntity<Void>deleteById(@PathVariable Long id){
        var animeToBeDeleted = Anime.getAnimes().stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Anime not found my brotha"));
        Anime.getAnimes().remove(animeToBeDeleted);
        return ResponseEntity.noContent().build();
    }
}

