package com.gw.database;

import com.gw.jpa.GWUser;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<GWUser, String> {

    Optional<GWUser> findByUsername(String username);

}
