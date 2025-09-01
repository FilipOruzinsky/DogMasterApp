package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.entity.Dog;
import com.example.dogmasterapp.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dogs")
@RequiredArgsConstructor
public class DogController {
    private final DogService dogService;

    @PostMapping
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        return ResponseEntity.ok(dogService.createDog(dog));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDogById(@PathVariable Integer id) {
        return ResponseEntity.ok(dogService.getDogById(id));
    }

    @GetMapping
    public ResponseEntity<List<Dog>> getAllDogs() {
        return ResponseEntity.ok(dogService.getAllDogs());
    }

    @GetMapping("/owner/{id}")
    public ResponseEntity<List<Dog>> getDogsByOwner(@PathVariable String id) {
        return ResponseEntity.ok(dogService.getDogsByOwner(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable Integer id, @RequestBody Dog dog) {
        return ResponseEntity.ok(dogService.updateDog(id, dog));
    }
    @PutMapping("/changeOwner/{dogID}")
    public ResponseEntity<Dog> changeOwner(@PathVariable Integer dogID) {
        return ResponseEntity.ok(dogService.changeOwner(dogID));
    }



    @DeleteMapping("/{id}")
    public void deleteDogById(@PathVariable Integer id) {
        dogService.deleteDogById(id);
    }
}
