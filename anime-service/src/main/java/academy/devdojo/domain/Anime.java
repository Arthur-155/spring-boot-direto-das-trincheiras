package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.util.List;

@Getter
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;

    public static List<Anime>animeList(){
        var katekyoHitmanReborn = new Anime(1L, "Katekyo Hitman Reborn");
        var CodeGeass = new Anime(2L, "Code Geass");
        var Pokemon = new Anime(3L, "Pokémon");
        return List.of(katekyoHitmanReborn,CodeGeass,Pokemon);
    }


}
