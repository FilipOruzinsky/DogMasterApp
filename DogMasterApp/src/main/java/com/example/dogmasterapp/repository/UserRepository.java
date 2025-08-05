package com.example.dogmasterapp.repository;

import com.example.dogmasterapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

     User findUserByUserID(Integer userId);
     User findUserByEmail(String email);

}
