package com.franmowat.habittracker.repository;

import com.franmowat.habittracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
