package br.com.arthub.ah_rest_art.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_art.constants.ArtStatus;
import br.com.arthub.ah_rest_art.dto.ApiResponse;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.dto.UpdateArtImageRefPayload;
import br.com.arthub.ah_rest_art.dto.UpdateArtPayload;
import br.com.arthub.ah_rest_art.dto.ArtData;
import br.com.arthub.ah_rest_art.entity.ArtEntity;
import br.com.arthub.ah_rest_art.feign.client.UserAccountFeignClient;
import br.com.arthub.ah_rest_art.repository.ArtRepository;
import br.com.arthub.ah_rest_art.utils.DateUtils;

@Service
public class ArtService {
	@Autowired
	private ArtRepository artRepository;
	@Autowired
	private UserAccountFeignClient accountFeignClient;
	@Autowired
	private ArtImageReferenceService imgRefService;
	
	@Value("${arthub.ms.secrets.user-id-by-token}")
	private String secretCallUserIdByToken;
	
	/**
	 * @param artPayload
	 * 
	 * <p>Registra uma arte no sistema.</p>
	 * <p>Por padrão toda arte é registrada inicialmente como privada.</p>
	 * <p>Esse método faz uma chamada no microserviço de usuários requisitando </p>
	 * */
	public String doCreateAnArt(ArtPayload artPayload, String token) {
		UUID accountId = getUserAccountIdByToken(token);
		if(accountId != null) {
			create(artPayload, accountId);
			return "Art created successfully.";
		}
		else
			throw new RuntimeException("User account not found.");
	}
	
	/**
	 * @param userAccountId
	 * 
	 * <p>Busca dados de todas as artes de um usuário artista.</p>
	 * */
	public List<ArtData> getAllUserArts(String tokenJwt) {
		UUID userAccountId = getUserAccountIdByToken(tokenJwt);
		if(userAccountId != null) {
			List<ArtData> arts = artRepository.getAllUserArts(userAccountId).stream().map(a -> {
				a.setImgRefs(imgRefService.getAllArtImgRefs(a.getArtId()));
				a.setCreatedAtText(DateUtils.timeAgo(a.getCreatedAt()));
				a.setLastModifiedText(DateUtils.timeAgo(a.getLastModified()));
				return a;
			}).toList();
			return arts;
		}
		else
			throw new RuntimeException("User account not found.");
	}
	
	/**
	 * @param artId
	 * @param tokenJwt
	 * @param uploadArtPayload
	 * 
	 * <p>Atualiza dados de uma arte no sistema.</p>
	 * <p>É importante ressaltar que esse método atualiza apenas informações da entidade arte,</p>
	 * <p>as atualizações das referências e outras entidades filhas da arte são realizadas
	 * <span>separadamente.</span>
	 * </p>
	 * */
	public String doUpdateArt(UUID artId, String tokenJwt, UpdateArtPayload updateArtPayload) {
		UUID accountId = getUserAccountIdByToken(tokenJwt);
		validateArtAndAction(artId, accountId);
		ArtPayload payload = new ArtPayload(updateArtPayload);
		validateArtPayload(payload);
		
		ArtEntity art = new ArtEntity(payload);
		art.setArtId(artId);
		art.setUserAccountId(accountId);
		
		if(!updateArtPayload.getHaveSchedule()) {
			art.setStartScheduleDate(null);
			art.setEndScheduleDate(null);
		}
		
		ArtEntity registred = artRepository.saveAndFlush(art);
		if(registred == null)
			throw new RuntimeException("Unable to insert art into the system.");
		
		return "Artwork data updated successfully.";
	}
	
	/**
	 * @param artId
	 * @param tokenJwt
	 * 
	 * <p>Deleta por completo uma arte no sistema.</p>
	 * <p>Esse método deleta todas as entidades filhas da arte e por fim a arte.</p>
	 * */
	public void doFullDeleteArt(UUID artId, String tokenJwt) {
		Optional<ArtEntity> opArt = validateArtAndAction(artId, getUserAccountIdByToken(tokenJwt));
		imgRefService.doDeleteAllRefsIfExists(artId);
		artRepository.delete(opArt.get());
	}
	
	/**
	 * @param artId
	 * @param tokenJwt
	 * @param updatePayload
	 * 
	 * <p>Atualiza todas as referências de imagens de uma arte no sistema.</p>
	 * */
	public String doUpdateAllArtImageRefs(UUID artId, String tokenJwt, UpdateArtImageRefPayload updatePayload) {
		validateArtAndAction(artId, getUserAccountIdByToken(tokenJwt)); 
		if(updatePayload.getArtImageRef().isEmpty())
			throw new RuntimeException("The list of reference images cannot be empty.");
		
		imgRefService.updateImageRefToArt(updatePayload.getArtImageRef());
		return "Reference images updated successfully.";
	}
	
	/**
	 * @param tokenJwt
	 * @param artId
	 * @param status
	 * 
	 * <p>Avança ou recua o status de uma arte registrada no sistema.</p>
	 * */
	public String doChangeArtwork(String tokenJwt, UUID artId, String strStatus) {
		ArtEntity art = validateArtAndAction(artId, getUserAccountIdByToken(tokenJwt)).get();
        ArtStatus newStatus = convertStringToArtStatus(strStatus);
        ArtStatus currentStatus = art.getArtStatus();

        validateStatusChange(currentStatus, newStatus);

		art.setArtStatus(newStatus);
		this.artRepository.saveAndFlush(art);

		return "Status changed successfully.";
	}
	
	
	/* Private Methods */
	
	private void validateStatusChange(ArtStatus currentStatus, ArtStatus newStatus) {
	    switch (newStatus) {
	        case TODO:
	            throw new RuntimeException("It is not possible to change to \"to do\" status once artwork has started.");
	        
	        case PROGRESS:
	            if (currentStatus == ArtStatus.TODO || currentStatus == ArtStatus.DRAWNER || currentStatus == ArtStatus.FINISHED) {
	                break;
	            }
	            throw new RuntimeException("The artwork is already in progress.");
	        
	        case FINISHED:
	            if (currentStatus == ArtStatus.PROGRESS) {
	                break; 
	            }
	            if (currentStatus == ArtStatus.DRAWNER) {
	                throw new RuntimeException("Cannot finish artwork directly from shelved state. Move to progress first.");
	            }
	            if (currentStatus == ArtStatus.FINISHED) {
	                throw new RuntimeException("Artwork is already finished.");
	            }
	            throw new RuntimeException("Artwork must be in progress to finish.");
	        
	        case DRAWNER:
	            if (currentStatus == ArtStatus.PROGRESS) {
	                break; 
	            }
	            if (currentStatus == ArtStatus.FINISHED) {
	                throw new RuntimeException("Cannot shelve artwork. It's already finished.");
	            }
	            if (currentStatus == ArtStatus.DRAWNER) {
	                throw new RuntimeException("Artwork is already shelved.");
	            }
	            throw new RuntimeException("Artwork must be in progress to shelve.");

	        default:
	            throw new RuntimeException("Unknown status: " + newStatus);
	    }
	}
	
	private ArtStatus convertStringToArtStatus(String status) {
       for (ArtStatus artStatus : ArtStatus.values()) {
            if (artStatus.getArtStatusName().equalsIgnoreCase(status)) {
                return artStatus;
            }
        }
        throw new IllegalArgumentException("Invalid status. An artwork can have the status of: TODO, PROGRESS, FINISHED and DRAWNER.");
	}      
	
	private UUID getUserAccountIdByToken(String tokenJwt) {
		UUID accountId = null;
		
		if(tokenJwt.isBlank() || !tokenJwt.startsWith("Bearer "))
			throw new RuntimeException("The jwt token is required.");
		
		String token = tokenJwt.substring(7);
		try {
			ResponseEntity<ApiResponse> response =  accountFeignClient.getUserIdByToken(token, secretCallUserIdByToken);
			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
	            throw new RuntimeException("User account not found or the user account service could not be called.");
	        }
			
			accountId = UUID.fromString((String) response.getBody().getData());
		} catch (Exception e) {
			throw new RuntimeException("User account not found.");
		}
		
		return accountId;
	}
	
	private void create(ArtPayload payload, UUID accountId) {
		validateArtPayload(payload);
		imgRefService.validateImgRefPayload(payload);
		
		ArtEntity art = new ArtEntity(payload);
		art.setUserAccountId(accountId);
		ArtEntity registred = artRepository.saveAndFlush(art);
		if(registred == null)
			throw new RuntimeException("Unable to insert art into the system.");
		
		imgRefService.createImageRefToArt(payload, registred);
	}
	
	private Optional<ArtEntity> validateArtAndAction(UUID artId, UUID accountId) {
		Optional<ArtEntity> opArt = artRepository.findById(artId);
		if(opArt.isEmpty())
			throw new RuntimeException("Art not found.");
		if(!opArt.get().getUserAccountId().equals(accountId))
			throw new RuntimeException("Action denied.");
		return opArt;
	}
	
	private void validateArtPayload(ArtPayload payload) {
		if(payload.getHaveSchedule() == null)
			throw new RuntimeException("The \"have schedule\" value is required.");
		else if(payload.getArtName().isEmpty())
			throw new RuntimeException("The \"art name\" value is required.");
		
		if(payload.getHaveSchedule() && payload.getStartScheduleDate() == null)
			throw new RuntimeException("The \"start shcedule date\" is required.");
		else if(payload.getHaveSchedule() && payload.getEndScheduleDate() == null)
			throw new RuntimeException("The \"end shcedule date\" is required.");
	}
}
