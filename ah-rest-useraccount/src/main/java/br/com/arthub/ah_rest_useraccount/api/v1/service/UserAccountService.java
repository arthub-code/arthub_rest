package br.com.arthub.ah_rest_useraccount.api.v1.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRepository;
import br.com.arthub.ah_rest_useraccount.api.v1.utils.JwtUtils;

@Service
public class UserAccountService {
	@Autowired
	private UserAccountRepository repository;
	@Autowired
	private JwtUtils jwtUtils;
	
	public Boolean doCheckIfAccountExists(UUID accountId) {
		return !(repository.findById(accountId).isEmpty());
	}
	
	public UUID getUserAccountIdByToken(String token) {
		if(jwtUtils.isTokenExpired(token))
			throw new RuntimeException("Token is expired.");
		
		Optional<UserAccountEntity> opUser =  repository.findByEmail(jwtUtils.extractUsername(token));
		if(opUser.isEmpty())
			throw new RuntimeException("User account not found.");
		
		return opUser.get().getUserAccountId();
	}
}
