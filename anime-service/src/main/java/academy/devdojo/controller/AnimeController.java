package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/animes")
public class AnimeController {

    @GetMapping
    public List<Anime> ListAll(@RequestParam(required = false) String nome) {
        var animes = Anime.animeList;
        if(nome == null) return animes;

        return animes.stream().filter(anime -> anime.getName().equalsIgnoreCase(nome)).toList();
    }

    @GetMapping("{id}")
    public Anime findById(@PathVariable Long id) {
        return Anime.animeList.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst().orElse(null);
    }

    @PostMapping
    public Anime createAnime(@RequestBody Anime anime){
        Long id = ThreadLocalRandom.current().nextLong(1,1000);
        anime.setId(id);
        Anime.animeList.add(anime);
        return anime;
    }
}

