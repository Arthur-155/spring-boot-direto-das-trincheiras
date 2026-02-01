package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AnimePutRequest {
    @NotNull(message = "the field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "the field 'name' is required")
    private String name;
    private LocalDateTime createdAt;
}
