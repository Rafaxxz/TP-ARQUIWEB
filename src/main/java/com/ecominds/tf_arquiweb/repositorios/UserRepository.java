package com.ecominds.tf_arquiweb.repositorios;

import com.ecominds.tf_arquiweb.entidades.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    
    Optional<Users> findByUsername(String username);
    
    Optional<Users> findByEmail(String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
}
