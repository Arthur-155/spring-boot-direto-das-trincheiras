package academy.devdojo.mapper;

import academy.devdojo.domain.Anime;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimesMapper {
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1,1000))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Anime toAnime(AnimePostRequest animePostRequest);

    AnimePostResponse toAnimePostResponse(Anime anime);

    Anime toPutAnime(AnimePutRequest animePutRequest);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animes);
}
