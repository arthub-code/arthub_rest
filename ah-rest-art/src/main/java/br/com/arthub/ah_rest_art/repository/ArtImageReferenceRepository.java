package br.com.arthub.ah_rest_art.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_art.dto.ArtImageRefData;
import br.com.arthub.ah_rest_art.entity.ArtImageReferenceEntity;
import jakarta.transaction.Transactional;

@Repository
public interface ArtImageReferenceRepository extends JpaRepository<ArtImageReferenceEntity, UUID>{
	@Query(" SELECT new br.com.arthub.ah_rest_art.dto.ArtImageRefData( 		       \n" + 
           "  	ref.artImageReferenceId, ref.imageLink, ref.uploadType 			   \n" + 
		   " ) FROM ArtImageReferenceEntity ref WHERE ref.artParent.artId = :artId   "
    )
	List<ArtImageRefData> getAllArtImgRefs(@Param(value = "artId") UUID artId);
	
	@Query(" SELECT ref FROM ArtImageReferenceEntity ref WHERE ref.artParent.artId = :artId")
	List<ArtImageReferenceEntity> findAllArtImgRefs(@Param(value = "artId") UUID artId);
	
	@Modifying
	@Transactional
	@Query(" DELETE FROM ArtImageReferenceEntity ref WHERE ref.artParent.artId = :artId ")
	void clearAll(@Param(value = "artId") UUID artId);
}
