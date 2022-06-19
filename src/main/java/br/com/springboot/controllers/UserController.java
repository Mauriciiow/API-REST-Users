package br.com.springboot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.model.User;
import br.com.springboot.repository.UserRepository;
import lombok.var;

@RestController
@RequestMapping("/")

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/{id}")
    public User user(@PathVariable("id") Long id){

        Optional<User> userFind = this.userRepository.findById(id);

        if (userFind.isPresent()) {
            return userFind.get();
        }

        return null;

    }

    @PostMapping("/add_users")
    public User addUser(@RequestBody User user){
        return this.userRepository.save(user);
       
    }

    @GetMapping("/users")
    public List<User> list() {
        return this.userRepository.findAll();
    }

    @GetMapping("/users_more/{id}")
    public List<User> listMoreThan(@PathVariable("id") Long id) {
        return this.userRepository.findByIdGreaterThan(id);
    }

    @GetMapping("/users_name/{name}")
    public List<User> listByName(@PathVariable("name") String name) {
        return this.userRepository.findByNameIgnoreCase(name);
    }

    @PatchMapping("/edit_user/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {

        Optional<User> userExist = this.userRepository.findById(id);

        if (!userExist.isPresent()) {
            return null;
        }

        User userUpdate = userExist.get();
        userUpdate.setName(user.getName());
        userUpdate.setUsername(user.getUsername());
        userRepository.save(userUpdate);
        return userUpdate;
       
    }
}
