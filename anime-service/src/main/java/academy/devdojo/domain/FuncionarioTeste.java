package academy.devdojo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class FuncionarioTeste {
    private Long id;
    private String name;
    private String cargo;
    private LocalDateTime createdAt;
    private static List<FuncionarioTeste> funcionarios = new ArrayList<>();

    static {
        var Arthur = FuncionarioTeste.builder().id(1L).name("Arthur Vieira").cargo("Analista Trainee").createdAt(LocalDateTime.now()).build();
        funcionarios.add(Arthur);
    }

    public static List<FuncionarioTeste> getFuncionarios() {
        return funcionarios;
    }
}
