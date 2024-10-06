package br.com.arthub.ah_rest_useraccount.api.v1.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRepository;

@Service
public class UserAccountService {
	@Autowired
	private UserAccountRepository repository;
	
	public Boolean doCheckIfAccountExists(UUID accountId) {
		return !(repository.findById(accountId).isEmpty());
	}
}
