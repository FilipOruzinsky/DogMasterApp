package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Dog;
import com.example.dogmasterapp.repository.DogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DogService {
    private final DogRepository dogRepository;
    private final UserService userService;

    public Dog createDog(Dog dog) {
        dog.setOwner(userService.getCurrentUser());

        return dogRepository.save(dog);
    }

    public Dog getDogById(Integer dogId) {
        return dogRepository.findById(dogId).orElse(null);
    }

    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

    public List<Dog> getDogsByOwner(String ownerId) {
        return dogRepository.findAllByOwner_UserID(ownerId);
    }

    public Dog updateDog(Integer dogId, Dog dog) {
        Dog existingDog = dogRepository.findById(dogId).orElseThrow(() -> new EntityNotFoundException("Dog not found with id: " + dogId));

        existingDog.setAge(dog.getAge());
        existingDog.setBreed(dog.getBreed());
        existingDog.setName(dog.getName());

        return dogRepository.save(existingDog);
    }

    public void deleteDogById(Integer dogId) {
        dogRepository.deleteById(dogId);
    }
}
