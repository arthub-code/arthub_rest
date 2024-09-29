package br.com.arthub.ah_rest_useraccount.api.v1.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountRequestEntity;

@Repository
public interface UserAccountRequestRepository extends JpaRepository<UserAccountRequestEntity, UUID>{
    @Query("SELECT u FROM UserAccountRequestEntity u WHERE u.accountEmail = :email")
    UserAccountRequestEntity findByEmail(@Param(value = "email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserAccountRequestEntity u WHERE u.accountUsername = :username")
    Boolean usernameInUse(@Param(value = "username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserAccountRequestEntity u WHERE u.accountEmail = :email")
    Boolean emailInUse(@Param(value = "email") String email);
}