package com.example.WebServerLocalElection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByCode(String code);
    User findByEmail(String email);
    User findByCode(String code);

}
