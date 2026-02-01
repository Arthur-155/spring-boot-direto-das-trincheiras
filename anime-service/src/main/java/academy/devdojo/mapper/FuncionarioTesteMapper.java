package academy.devdojo.mapper;

import academy.devdojo.domain.FuncionarioTeste;
import academy.devdojo.request.FuncionarioPostRequest;
import academy.devdojo.response.FuncionarioGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FuncionarioTesteMapper {

    FuncionarioTesteMapper INSTACE = Mappers.getMapper(FuncionarioTesteMapper.class);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    FuncionarioTeste toFuncionario(FuncionarioPostRequest funcionarioPostRequest);

    FuncionarioGetResponse toFuncionarioGetResponse(FuncionarioTeste funcionarioTeste);

    List<FuncionarioGetResponse> toFuncionarioTesteList(List<FuncionarioTeste> funcionarios);
}
