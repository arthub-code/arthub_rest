package br.com.arthub.ah_rest_comission.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_comission.domain.entity.ServiceEntity;
import br.com.arthub.ah_rest_comission.dto.GenericResponse;
import br.com.arthub.ah_rest_comission.dto.ServiceData;
import br.com.arthub.ah_rest_comission.dto.ServicePayload;
import br.com.arthub.ah_rest_comission.factory.ServiceFactory;
import br.com.arthub.ah_rest_comission.repository.ServiceRepository;
import br.com.arthub.ah_rest_comission.utils.UserAccountMSUitls;

@Service
public class ServiceService {
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private UserAccountMSUitls accountMsUtils;
	
	@Autowired
	private ServiceFactory serviceFactory;
	
	@Autowired
	private ServiceTagService tagService;
	
	public GenericResponse doCreate(String jwtToken, ServicePayload payload) throws Exception {
		UUID artistId = accountMsUtils.getArtistIdByToken(jwtToken);
		payload.setArtistId(artistId);
		ServiceEntity entity = serviceFactory.createEntity(payload);	
		serviceRepository.save(entity);
		
		List<String> tags = new ArrayList<String>();
		if(!payload.getTags().isEmpty()) 
			tags = tagService.createAllTags(payload.getTags(), entity);
		
		return new GenericResponse(new ServiceData(entity, tags), "The service was created successfully!");
	}
	
	/* private methods */
	
	private Optional<ServiceEntity> validateServiceAndAction(UUID serviceId, UUID artistId) {
		Optional<ServiceEntity> opArt = serviceRepository.findById(serviceId);
		if(opArt.isEmpty())
			throw new RuntimeException("Service not found.");
		if(!opArt.get().getProvidedBy().equals(artistId))
			throw new RuntimeException("Action denied.");
		return opArt;
	}
}
