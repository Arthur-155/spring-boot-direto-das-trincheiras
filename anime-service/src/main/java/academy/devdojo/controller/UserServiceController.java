package academy.devdojo.controller;

import academy.devdojo.domain.UserService;
import academy.devdojo.mapper.UserServiceMapper;
import academy.devdojo.request.UserServicePostRequest;
import academy.devdojo.request.UserServicePutRequest;
import academy.devdojo.response.UserServiceGetResponse;
import academy.devdojo.response.UserServicePostResponse;
import academy.devdojo.service.UserServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users")
public class UserServiceController {
    private final UserServiceService service;
    private final UserServiceMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserServiceGetResponse>> findAll(@RequestParam(required = false) String name){
        var users = service.findAll(name);
        var response = mapper.userServiceGetResponseList(users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserServiceGetResponse>findById(@PathVariable Long id){
        var expectedUser = service.findByIdOrThrowNotFound(id);
        var response = mapper.userServiceGetResponse(expectedUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserServicePostResponse>save(@RequestBody @Valid UserServicePostRequest userServicePostRequest){
        var userToSave = mapper.toUserService(userServicePostRequest);
        var save = service.save(userToSave);
        var response = mapper.userServicePostResponse(save);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void>deleteById(UserService userService){
        service.deleteByIdOrThrowNotFound(userService);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserServicePutRequest request){
        var putRequest = mapper.toUserServicePutRequest(request);
        service.updateOrThrowNotFound(putRequest);
        return ResponseEntity.noContent().build();
    }
}
