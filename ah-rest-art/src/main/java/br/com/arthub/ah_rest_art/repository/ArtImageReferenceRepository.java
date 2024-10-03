package br.com.arthub.ah_rest_art.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_art.entity.ArtImageReferenceEntity;

@Repository
public interface ArtImageReferenceRepository extends JpaRepository<ArtImageReferenceEntity, UUID>{

}
