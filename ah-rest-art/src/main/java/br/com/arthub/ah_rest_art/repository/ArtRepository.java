package br.com.arthub.ah_rest_art.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arthub.ah_rest_art.dto.ArtData;
import br.com.arthub.ah_rest_art.entity.ArtEntity;

@Repository
public interface ArtRepository extends JpaRepository<ArtEntity, UUID> {
	@Query(
		    " SELECT new br.com.arthub.ah_rest_art.dto.ArtData(                \n" +
		    "         a.artId, a.artName, a.artLevel, a.artStatus,             \n" +
		    "         a.haveSchedule, a.startScheduleDate, a.endScheduleDate,  \n" +
		    "         a.createdAt, a.updatedAt		                           \n" +
		    "     )                                                            \n" + 
		    " FROM ArtEntity a WHERE a.userAccountId = :accountId			   \n" 
		)
	List<ArtData> getAllUserArts(@Param(value = "accountId") UUID accountId);
	
	@Query(
			" SELECT new br.com.arthub.ah_rest_art.dto.ArtData(                \n" +
		    "         a.artId, a.artName, a.artLevel, a.artStatus,             \n" +
		    "         a.haveSchedule, a.startScheduleDate, a.endScheduleDate,  \n" +
		    "         a.createdAt, a.updatedAt		                           \n" +
		    "     )                                                            \n" + 
		    " FROM ArtEntity a WHERE a.artId = :artId			  			   \n" 
			)
	ArtData getArtDataById(@Param(value = "artId") UUID artId);
}
