package br.com.arthub.ah_rest_comission.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_comission.domain.entity.ServiceEntity;
import br.com.arthub.ah_rest_comission.factory.ServiceTagFactory;
import br.com.arthub.ah_rest_comission.repository.ServiceTagRepository;

@Service
public class ServiceTagService {
	@Autowired
	private ServiceTagRepository tagRepository;
	
	@Autowired
	private ServiceTagFactory tagFactory;
	
	public List<String> createAllTags(List<String> tags, ServiceEntity serviceParent) {
		tagRepository.saveAll(tagFactory.createEntityList(tags, serviceParent));
		return tags;
	}
}
