package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodedRepository {
    private final Connection connectionMongo;
    private final ProducerData producerData;

    public List<Producer> findAll() {
        return producerData.getProducers();
    }

    public List<Producer>findByName(String nome){
        log.debug(connectionMongo);
        return producerData.getProducers()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(nome))
                .toList();
    }

    public Optional<Producer>findById(Long id){
        return producerData.getProducers()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Producer save (Producer producer){
        producerData.getProducers().add(producer);
        return producer;
    }

    public void deleteById (Producer producer){
        producerData.getProducers().remove(producer);
    }

    public void update (Producer producer){
        deleteById(producer);
        save(producer);
    }
}
