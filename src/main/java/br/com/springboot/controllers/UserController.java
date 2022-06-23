package br.com.springboot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
       
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

      @GetMapping("/users/{id}")
        ResponseEntity<Object> user(@PathVariable("id") Long id){

        var userFind = userService.findById(id);

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

    @PatchMapping("/edit_user/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody User user) {

        var userExist = userService.findById(id);

        if (!userExist.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var newUser = userExist.get();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setTelefone(user.getTelefone());
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.save(newUser));

       
    }

    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id){
        var userExist = userService.findById(id);

        if (!userExist.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
        

        
    }
}


