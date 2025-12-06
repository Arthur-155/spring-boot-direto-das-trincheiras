package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    {
        var katekyoHitmanReborn = Anime.builder().id(1L).name("Katekyo Hitman Reborn").createdAt(LocalDateTime.now()).build();
        var codeGeass = Anime.builder().id(2L).name("Code Geass").createdAt(LocalDateTime.now()).build();
        var pokemon = Anime.builder().id(3L).name("Pokémon").createdAt(LocalDateTime.now()).build();
        animes.addAll(List.of(katekyoHitmanReborn,codeGeass,pokemon));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
