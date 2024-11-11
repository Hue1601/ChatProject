package com.example.routervuebe.controller;

import com.example.routervuebe.entity.Users;
import com.example.routervuebe.repo.ConversationsRepo;
import com.example.routervuebe.repo.MessagesRepo;
import com.example.routervuebe.repo.UsersRepo;

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
    private UsersRepo usersRepo;

    @Autowired
    private MessagesRepo messagesRepo;

    @Autowired
    private ConversationsRepo conversationsRepo;

    @GetMapping("/list")
    public List<Users> list(){
        return usersRepo.findAll();
    }
    @PostMapping("/add-user")
    public Users adduser(@Valid @RequestBody Users users){
        usersRepo.save(users);
        return users;
    }
    @GetMapping("/push-update-user/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        return usersRepo.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update-user/{id}")
    public Users updateusers(@PathVariable Integer id, @RequestBody Users users){
        users.setId(id);
        usersRepo.save(users);
        return users;
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteuser(@PathVariable Integer id){
        usersRepo.deleteById(id);
    }

    @GetMapping("/search")
    public List<Users> search(@RequestParam(defaultValue = "") String keyword){
        return usersRepo.search("%"+keyword+"%");
    }
    @GetMapping("/pagination")
    public Map<String, Object> phantrang(@RequestParam(defaultValue = "0") int p) {
    Pageable pageable = PageRequest.of(p, 8); // Page starts from 0
    Page<Users> page = usersRepo.findAll(pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("users", page.getContent());
    response.put("totalPages", page.getTotalPages());
    response.put("currentPage", page.getNumber());

    return response;
}


}


