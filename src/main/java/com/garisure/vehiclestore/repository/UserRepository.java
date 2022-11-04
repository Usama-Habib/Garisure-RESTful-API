package com.garisure.vehiclestore.repository;

import com.garisure.vehiclestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE users u SET u.id_token = ? WHERE u.username = ?", nativeQuery = true)
    int updateUserToken(String idToken, String username);
    Optional<User> findByUsername(String userName);
    User getUserByUsername(String username);
    Boolean existsByUsername (String username);
    public User findUserById(Long id);
}
