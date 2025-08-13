package com.example.dogmasterapp.repository;

import com.example.dogmasterapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

     Optional <User> findUserByUserID(String userId);
     Optional <User> findUserByEmail(String email);

}
