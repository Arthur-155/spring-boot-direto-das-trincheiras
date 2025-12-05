package academy.devdojo.mapper;

import academy.devdojo.domain.Producer;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {
    ProducerMapper INSTACE = Mappers.getMapper(ProducerMapper.class);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Producer toProducer(ProducerPostRequest postRequest);

    ProducerPostResponse toProducerPostResponse (Producer producer);

    Producer toPutProducer(ProducerPutRequest producerPutRequest);

    ProducerGetResponse toProducerGetResponse (Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer>producersList);
}
