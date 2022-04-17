package com.example.restfulwebservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJpaController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    // http://localhost:8088/jpa/users
    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        EntityModel entityModel = EntityModel.of(user.get());

        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkTo.withRel("all-users"));

        return entityModel; // .get()은 Optional에서 가져옴
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id)
    {
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user)
    {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(savedUser.getId()) // savedUser의 id를 path로 지정한다 뭐 그런거 같음,,
                .toUri(); // Uri화 함

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id)
    {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return user.get().getPosts(); // getter로 가져온것임
    }

    @GetMapping("/users/{userId}/posts/{postId}") // 개별 포스트 조회
    public Optional<Post> retrieveOnePostsByUser(@PathVariable int userId, @PathVariable int postId)
    {
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", userId));
        }

        return user.get().getPosts().stream()
                .filter(p -> p.getId() == postId)
                .findAny(); // getter로 가져온것임
    }

    @PutMapping("/users/{id}") // 회원정보 수정
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        User storedUser = optionalUser.get();
        storedUser.setName(user.getName());
        storedUser.setPassword(user.getPassword());

        User updatedUser = userRepository.save(storedUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    
    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id , @RequestBody Post post)
    {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        post.setUser(user.get()); // 해당하는 유저 찾아서 게시물에 userId 넣기!

        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
