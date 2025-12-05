package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {
    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        var katekyoHitmanReborn = Anime.builder().id(1L).name("Katekyo Hitman Reborn").createdAt(LocalDateTime.now()).build();
        var codeGeass = Anime.builder().id(2L).name("Code Geass").createdAt(LocalDateTime.now()).build();
        var pokemon = Anime.builder().id(3L).name("Pokémon").createdAt(LocalDateTime.now()).build();
        ANIMES.addAll(List.of(katekyoHitmanReborn,codeGeass,pokemon));
    }

    public static List<Anime>findAll(){
        return ANIMES;
    }

    public List<Anime>findByName(String name){
        return ANIMES.
                stream().
                filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Optional<Anime> findById(Long id){
        return ANIMES
                .stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public Anime save(Anime anime){
        ANIMES.add(anime);
        return anime;
    }

    public void delete(Anime anime){
        ANIMES.remove(anime);
    }

    public void update(Anime anime){
        delete(anime);
        save(anime);
    }
}
