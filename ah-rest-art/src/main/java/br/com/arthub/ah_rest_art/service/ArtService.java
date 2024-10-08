package br.com.arthub.ah_rest_art.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_art.dto.ApiResponse;
import br.com.arthub.ah_rest_art.dto.ArtPayload;
import br.com.arthub.ah_rest_art.entity.ArtEntity;
import br.com.arthub.ah_rest_art.feign.client.UserAccountFeignClient;
import br.com.arthub.ah_rest_art.repository.ArtRepository;

@Service
public class ArtService {
	@Autowired
	private ArtRepository artRepository;
	@Autowired
	private UserAccountFeignClient accountFeignClient;
	@Autowired
	private ArtImageReferenceService imgRefService;
	
	/**
	 * @param artPayload
	 * 
	 * <p>Registra uma arte no sistema.</p>
	 * <p>Por padrão toda arte é registrada inicialmente como privada.</p>
	 * <p>Esse método faz uma chamada no microserviço de usuários requisitando </p>
	 * */
	public String doCreateAnArt(ArtPayload artPayload) {
		if(artPayload.getAccountId() == null)
			throw new RuntimeException("user account id is required.");
		
		ResponseEntity<ApiResponse> response = accountFeignClient.userAccountExists(artPayload.getAccountId());
		if(response.getStatusCode().is2xxSuccessful()  
		   && (response.getBody().getData() instanceof Boolean)
		   && ((Boolean)response.getBody().getData())) {
			create(artPayload);
			return "Art created successfully.";
		}
		else
			throw new RuntimeException("user account not found.");
	}
	
	private void create(ArtPayload payload) {
		validateArtPayload(payload);
		imgRefService.validateImgRefPayload(payload);
		
		ArtEntity art = new ArtEntity(payload);
		ArtEntity registred = artRepository.saveAndFlush(art);
		if(registred == null)
			throw new RuntimeException("unable to insert art into the system.");
		
		imgRefService.createImageRefToArt(payload, registred);
	}
	
	private void validateArtPayload(ArtPayload payload) {
		if(payload.getHaveSchedule() == null)
			throw new RuntimeException("\"have schedule\" value is required.");
		else if(payload.getArtName().isEmpty())
			throw new RuntimeException("\"art name\" value is required.");
		
		if(payload.getHaveSchedule() && payload.getStartScheduleDate() == null)
			throw new RuntimeException("\"start shcedule date\" is required.");
		else if(payload.getHaveSchedule() && payload.getEndScheduleDate() == null)
			throw new RuntimeException("\"end shcedule date\" is required.");
	}
}
