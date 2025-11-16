package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;

    public static List<Anime>animeList = new ArrayList<>(
        List.of(
                new Anime(1L, "Katekyo Hitman Reborn"),
                new Anime(2L, "Code Geass"),
                new Anime(3L, "Pokémon")
        )
    );


}
