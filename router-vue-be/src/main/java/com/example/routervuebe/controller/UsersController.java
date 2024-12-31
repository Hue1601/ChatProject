package com.example.routervuebe.controller;

import com.example.routervuebe.entity.Users;
import com.example.routervuebe.repo.UserRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private CognitoService cognitoService;
//
//    @PostMapping("/login")
//    public String loginUser(@RequestBody Users request) {
//        return cognitoService.loginUser(request.getUsername(), request.getPass());
//    }
    @GetMapping("/list")
    public List<Users> list(){
        return userRepository.findAll();
    }
    @PostMapping("/add-user")
    public Users adduser(@Valid @RequestBody Users users){
        userRepository.save(users);
        return users;
    }
    @GetMapping("/push-update-user/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update-user/{id}")
    public Users updateusers(@PathVariable Integer id, @RequestBody Users users){
        users.setId(id);
        userRepository.save(users);
        return users;
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteuser(@PathVariable Integer id){
        userRepository.deleteById(id);
    }

    @GetMapping("/search")
    public List<Users> search(@RequestParam(defaultValue = "") String keyword){
        return userRepository.search("%"+keyword+"%");
    }
    @GetMapping("/pagination")
    public Map<String, Object> phantrang(@RequestParam(defaultValue = "0") int p) {
    Pageable pageable = PageRequest.of(p, 8); // Page starts from 0
    Page<Users> page = userRepository.findAll(pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("users", page.getContent());
    response.put("totalPages", page.getTotalPages());
    response.put("currentPage", page.getNumber());

    return response;
}


}


