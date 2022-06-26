package br.com.springboot.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.dtos.UserDto;
import br.com.springboot.model.User;
import br.com.springboot.repository.UserRepository;
import br.com.springboot.services.UserService;
import lombok.var;

@RestController
@CrossOrigin(origins = "x", maxAge = 3600)
@RequestMapping("/")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add_users")
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDto user){

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Email is alredy in use");
        }

        var userModel = new User();
        
        BeanUtils.copyProperties(user, userModel);
        userModel.setCreated_at(LocalDateTime.now(ZoneId.of("UTC")));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
       
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/users/{id}")
        ResponseEntity<Object> listOne(@PathVariable("id") Long id){

         Optional<User> userFind = userService.findById(id);

        if (userFind.isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).body(userFind.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

    }

    // @GetMapping("/users_more/{id}")
    // public List<User> listMoreThan(@PathVariable("id") Long id) {
    //     return this.userRepository.findByIdGreaterThan(id);
    // }

    // @GetMapping("/users_name/{name}")
    // public List<User> listByName(@PathVariable("name") String name) {
    //     return this.userRepository.findByNameIgnoreCase(name);
    // }

    @PutMapping("/edit_user/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserDto user) {

        Optional<User> userExist = userService.findById(id);

        if (!userExist.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // var userUpdated = userExist.get();
        // userUpdated.setName(user.getName());
        // userUpdated.setEmail(user.getEmail());
        // userUpdated.setTelefone(user.getTelefone());

        // FORMA ALTERNATIVA E MAIS SIMPLES DE FAZER:

        var userModel = new User();
        BeanUtils.copyProperties(user, userModel);
        userModel.setId(userExist.get().getId());
        userModel.setCreated_at(userExist.get().getCreated_at());
        
        return ResponseEntity.status(HttpStatus.OK).body(userService.save(userModel));

       
    }

    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id){
        Optional<User> userExist = userService.findById(id);

        if (!userExist.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.delete(userExist.get());
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
        

        
    }
}


