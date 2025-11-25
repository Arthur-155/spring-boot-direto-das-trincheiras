package academy.devdojo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FuncionarioPostRequest {
    private String name;
    private String cargo;
    private LocalDateTime createdAt;
}
