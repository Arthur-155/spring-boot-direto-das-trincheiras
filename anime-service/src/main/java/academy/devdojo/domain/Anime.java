package academy.devdojo.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private static List<Anime> animes = new ArrayList<>();

    static {
        var katekyoHitmanReborn = Anime.builder().id(1L).name("Katekyo Hitman Reborn").build();
        var codeGeass = Anime.builder().id(2L).name("Code Geass").build();
        var pokemon = Anime.builder().id(3L).name("Pokémon").build();
        animes.addAll(List.of(katekyoHitmanReborn,codeGeass,pokemon));
    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
