package com.example.demo.web;

import com.example.demo.domain.User;
import com.example.demo.domain.UserRepository;
import com.example.demo.dto.UserDto;
import com.example.demo.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;
    private final UserMapper mapper;

    @GetMapping
    public List<UserDto> list() {
        return mapper.toDtoList(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
        User saved = repository.save(mapper.toEntity(dto));
        UserDto body = mapper.toDto(saved);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(body);
    }
}
