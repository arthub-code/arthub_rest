package br.com.arthub.ah_rest_comission.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.arthub.ah_rest_comission.domain.entity.ServiceEntity;
import br.com.arthub.ah_rest_comission.dto.ServicePayload;
import br.com.arthub.ah_rest_comission.exception.NullAttributeExcpetion;
import br.com.arthub.ah_rest_comission.exception.NullObjectException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceFactory {
	public ServiceEntity createEntity(ServicePayload payload) throws Exception {
		try {
			validateServicePayload(payload);
			ServiceEntity entity = new ServiceEntity(payload);
			return entity;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new NullObjectException(e.getMessage());
		} 
	}
	
	/* Private Methods */
	private void validateServicePayload(ServicePayload payload) throws Exception {
		if(payload == null)
			throw new NullPointerException("Payload object cannot be null.");
		else if(payload.getName() == null || payload.getName().equals(""))
			throw new NullAttributeExcpetion("Service name is required.");
		else if(payload.getDescription() == null || payload.getDescription().equals(""))
			throw new IllegalArgumentException("Service description is required.");
		else if(payload.getServiceAmount() == null)
			throw new NullAttributeExcpetion("Service amount is required.");
		else if(payload.getArtistId() == null)
			throw new NullAttributeExcpetion("Artist id is required.");
		else if(payload.getTags() == null)
			payload.setTags(List.of());
		
		else if(payload.getName().length() > 60 || payload.getName().length() < 10)
			throw new IllegalArgumentException("The service name must be between 10 and 60 characters long.");
		else if(payload.getServiceAmount().intValue() == 0)
			throw new IllegalArgumentException("The service cannot be free.");
	}
}
