package com.franmowat.habittracker.repositories;

import com.franmowat.habittracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
