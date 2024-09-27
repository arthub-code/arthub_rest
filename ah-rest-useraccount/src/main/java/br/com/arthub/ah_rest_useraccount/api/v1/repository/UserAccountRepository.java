package br.com.arthub.ah_rest_useraccount.api.v1.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountEntity;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, UUID>{
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserAccountEntity u WHERE u.accountUsername = :username")
    Boolean usernameInUse(@Param(value = "username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserAccountEntity u WHERE u.accountEmail = :email")
    Boolean emailInUse(@Param(value = "email") String email);
}
