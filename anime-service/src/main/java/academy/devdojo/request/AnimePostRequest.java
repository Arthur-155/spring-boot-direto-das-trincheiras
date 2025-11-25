package academy.devdojo.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimePostRequest {
    private String name;
    private LocalDateTime createdAt;
}
