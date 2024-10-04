package br.com.arthub.ah_rest_useraccount.api.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountProfileEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountRequestEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountProfileRepository;

@Service
public class UserAccountProfileService {
	@Autowired
	private UserAccountProfileRepository profileRepository;

	public void doRegisterProfile(UserAccountRequestEntity req, UserAccountEntity account) {
		UserAccountProfileEntity profile = new UserAccountProfileEntity();
		profile.setAccountFullName(req.getAccountFullName());
		profile.setAccountParent(account);
		profile.setAccountDateOfBirth(req.getAccountDateOfBirth());
		
		if(profileRepository.saveAndFlush(profile) == null)
			throw new RuntimeException("An error occurred while creating the user profile. Please contact support for more relevant information.");
	}
}
