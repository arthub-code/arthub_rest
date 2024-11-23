package br.com.arthub.ah_rest_comission.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_comission.domain.entity.ServiceEntity;
import br.com.arthub.ah_rest_comission.domain.entity.ServiceTagEntity;

@Service
public class ServiceTagFactory {
	public List<ServiceTagEntity> createEntityList(List<String> tags, ServiceEntity serviceParent) {
		List<ServiceTagEntity> entityList = new ArrayList<ServiceTagEntity>();
		for(String tag : tags) {
			entityList.add(new ServiceTagEntity(tag, serviceParent));	
		}
		return entityList;
	}
}
