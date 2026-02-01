package academy.devdojo.commons.animes;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> getAnimeList() {
        var dateTime = "2026-01-02T22:08:58.1849778";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var katekyoHitmanReborn = Anime.builder().id(1L).name("Katekyo Hitman Reborn").createdAt(localDateTime).build();
        var codeGeass = Anime.builder().id(2L).name("Code Geass").createdAt(localDateTime).build();
        var pokemon = Anime.builder().id(3L).name("Pokémon").createdAt(localDateTime).build();
        return new ArrayList<>(List.of(katekyoHitmanReborn, codeGeass, pokemon));
    }

    public Anime newAnimeToSave() {
        return Anime.builder().id(56L).name("Solo Leveling").createdAt(LocalDateTime.now()).build();
    }
}
