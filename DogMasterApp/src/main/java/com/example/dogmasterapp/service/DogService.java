package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Dog;
import com.example.dogmasterapp.exception.DogNotFoundException;
import com.example.dogmasterapp.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogService {
    private final DogRepository dogRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(DogService.class);

    public Dog createDog(Dog dog) {
        dog.setOwner(userService.getCurrentUser());

        return dogRepository.save(dog);
    }

    public Dog getDogById(Integer dogId) {
        return dogRepository.findById(dogId).orElseThrow(() -> logAndThrow(dogId));
    }

    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

    public List<Dog> getDogsByOwner(String ownerId) {
        return dogRepository.findAllByOwner_UserID(ownerId);
    }

    public Dog updateDog(Integer dogId, Dog dog) {
        Dog existingDog = dogRepository.findById(dogId).orElseThrow(() -> logAndThrow(dogId));

        existingDog.setAge(dog.getAge());
        existingDog.setBreed(dog.getBreed());
        existingDog.setName(dog.getName());

        return dogRepository.save(existingDog);
    }

    public void deleteDogById(Integer dogId) {
        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> logAndThrow(dogId));
        dogRepository.delete(dog);
    }

    /**
     * Changes the owner of the specified dog to the currently authenticated user.
     *
     * @param dogId the ID of the dog whose ownership is to be changed
     * @return the updated Dog entity with the new owner
     * @throws DogNotFoundException if no dog is found with the given ID
     */
    public Dog changeOwner(Integer dogId) {
        Dog existingDog = dogRepository.findById(dogId).orElseThrow(() -> logAndThrow(dogId));
        existingDog.setOwner(userService.getCurrentUser());
        return dogRepository.save(existingDog);
    }

    private DogNotFoundException logAndThrow(Integer dogId) {
        logger.error("Dog not found with id: {}", dogId);
        return new DogNotFoundException("Dog not found with id: " + dogId);
    }
}
