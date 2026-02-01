package academy.devdojo.mapper;

import academy.devdojo.domain.UserService;
import academy.devdojo.request.UserServicePostRequest;
import academy.devdojo.request.UserServicePutRequest;
import academy.devdojo.response.UserServiceGetResponse;
import academy.devdojo.response.UserServicePostResponse;
import academy.devdojo.service.AnimeService;
import ch.qos.logback.core.model.ComponentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserServiceMapper {
    UserServiceMapper INSTANCE = Mappers.getMapper(UserServiceMapper.class);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000))")
    UserService toUserService(UserServicePostRequest postRequest);

    UserServiceGetResponse userServiceGetResponse (UserService userService);

    List<UserServiceGetResponse>userServiceGetResponseList(List<UserService> userServices);

    UserServicePostResponse userServicePostResponse(UserService postResponse);

    UserService toUserServicePutRequest(UserServicePutRequest request);

}
