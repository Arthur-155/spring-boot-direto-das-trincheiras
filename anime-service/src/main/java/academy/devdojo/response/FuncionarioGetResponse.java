package academy.devdojo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class FuncionarioGetResponse {
    private Long id;
    private String name;
    private String cargo;
    private LocalDateTime createdAt;
}
