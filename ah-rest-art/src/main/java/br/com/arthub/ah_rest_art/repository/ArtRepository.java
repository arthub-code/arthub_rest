package br.com.arthub.ah_rest_art.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_art.entity.ArtEntity;

@Repository
public interface ArtRepository extends JpaRepository<ArtEntity, UUID>{

}
