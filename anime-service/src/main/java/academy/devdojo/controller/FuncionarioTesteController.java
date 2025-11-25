package academy.devdojo.controller;

import academy.devdojo.domain.FuncionarioTeste;
import academy.devdojo.mapper.FuncionarioTesteMapper;
import academy.devdojo.request.FuncionarioPostRequest;
import academy.devdojo.response.FuncionarioGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/funcionarios")
public class FuncionarioTesteController {

    public static final FuncionarioTesteMapper MAPPER = FuncionarioTesteMapper.INSTACE;

    @GetMapping
    public ResponseEntity<List<FuncionarioGetResponse>> listAll(@RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String cargo) {
        var funcionarios = FuncionarioTeste.getFuncionarios();
        var responseList = MAPPER.toFuncionarioTesteList(funcionarios);
        if (funcionarios == null) return ResponseEntity.ok(responseList);
        var response = responseList.stream()
                .filter(f -> name == null || f.getName().equalsIgnoreCase(name))
                .filter(f -> cargo == null || f.getCargo().equalsIgnoreCase(cargo))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<FuncionarioGetResponse>findById(@PathVariable Long id){
        var response = FuncionarioTeste.getFuncionarios()
                .stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .map(MAPPER::toFuncionarioGetResponse)
                .orElse(null);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<FuncionarioGetResponse>funcionarioPost(@RequestBody FuncionarioPostRequest funcionarioPostRequest){
        var funcionario = MAPPER.toFuncionario(funcionarioPostRequest);
        var response = MAPPER.toFuncionarioGetResponse(funcionario);

        FuncionarioTeste.getFuncionarios().add(funcionario);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
