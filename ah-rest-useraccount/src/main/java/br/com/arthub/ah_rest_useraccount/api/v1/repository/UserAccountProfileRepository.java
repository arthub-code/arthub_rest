package br.com.arthub.ah_rest_useraccount.api.v1.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountProfileEntity;

@Repository
public interface UserAccountProfileRepository extends JpaRepository<UserAccountProfileEntity, UUID>{

}
