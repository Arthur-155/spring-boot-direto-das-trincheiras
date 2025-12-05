package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeHardCodedRepository repository;

    public List<Anime>listAll(String name){
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Anime findByIdOrThrowNotFound(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found My Brotha"));
    }

    public Anime save(Anime anime){
        return repository.save(anime);
    }

    public void deleteOrThrowNotFound(Long id){
        var delete = findByIdOrThrowNotFound(id);
        repository.delete(delete);
    }

    public void updateOrThrowNotFound(Anime anime){
        var updated = findByIdOrThrowNotFound(anime.getId());
        anime.setCreatedAt(updated.getCreatedAt());
        repository.update(anime);
    }
}
