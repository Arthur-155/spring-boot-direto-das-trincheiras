package academy.devdojo.mapper;

import academy.devdojo.domain.Anime;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AnimesMapper {
    AnimesMapper INSTANCE = Mappers.getMapper(AnimesMapper.class);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1,1000))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Anime toAnime(AnimePostRequest animePostRequest);

    AnimePostResponse toAnimePostResponse(Anime anime);

    AnimeGetResponse toAnimeGetResponse (Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime>animes);
}
