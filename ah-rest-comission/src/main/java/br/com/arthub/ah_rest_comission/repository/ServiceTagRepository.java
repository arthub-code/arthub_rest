package br.com.arthub.ah_rest_comission.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_comission.domain.entity.ServiceTagEntity;

@Repository
public interface ServiceTagRepository extends JpaRepository<ServiceTagEntity, UUID>{

}
