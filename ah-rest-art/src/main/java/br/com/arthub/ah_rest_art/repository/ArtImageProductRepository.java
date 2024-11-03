package br.com.arthub.ah_rest_art.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_art.entity.ArtImageProductEntity;

@Repository
public interface ArtImageProductRepository extends JpaRepository<ArtImageProductEntity, UUID>{
	@Query("SELECT CASE WHEN COUNT(imgP) > 0 THEN true ELSE false END FROM ArtImageProductEntity imgP WHERE imgP.artParent.artId = :artId")
	boolean existsByArtId(@Param(value = "artId") UUID artId);
	
	@Query("SELECT imgP FROM ArtImageProductEntity imgP WHERE imgP.artParent.artId = :artId")
	Optional<ArtImageProductEntity> getImageProductByArtId(@Param(value = "artId") UUID artId);
}
