package com.example.dogmasterapp.repository;

import com.example.dogmasterapp.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository extends JpaRepository<Dog, Integer> {
    List<Dog> findAllByOwner_UserID(String ownerId);
}
