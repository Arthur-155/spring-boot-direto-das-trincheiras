package academy.devdojo.service;

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerHardCodedRepository repository;


    public List<Producer>findAll(String name){
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Producer findByIdOrThrowNotFound(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found"));
    }

    public Producer save(Producer producer){
        return repository.save(producer);
    }

    public void deleteByIdOrThrowNotFound(Long id){
         var delete= findByIdOrThrowNotFound(id);
         repository.deleteById(delete);
    }

    public void updateOrThrowNotFound(Producer producer){
        var update = findByIdOrThrowNotFound(producer.getId());
        producer.setCreatedAt(update.getCreatedAt());
        repository.update(producer);
    }
}
