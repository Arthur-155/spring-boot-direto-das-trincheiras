package academy.devdojo.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimePostResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
