package br.com.arthub.ah_rest_useraccount.api.v1.config;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.arthub.ah_rest_useraccount.api.v1.constants.UserAccountType;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountProfileEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountProfileRepository;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Configuration
public class DataLoader {
	@Autowired
	private UserAccountRepository accountRepository;
	
	@Autowired
	private UserAccountProfileRepository profileRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@PostConstruct
	@Transactional
	public void loadData() {
		if(accountRepository.count() == 0) {
			UserAccountEntity account = new UserAccountEntity();
			account.setAccountSocialName("System Demo");
			account.setAccountEmail("systemdemo@gmail.com");
			account.setAccountPassword(encoder.encode("demosys"));
			account.setAccountUsername("@sysdemo");
			account.setAccountActive(true);
			account.setCreationDate(LocalDate.now());
			account.setCreationTime(LocalTime.now());
			account.setUserAccountType(UserAccountType.Sysdemo);
			
			account = accountRepository.save(account);
			
			UserAccountProfileEntity profile = new UserAccountProfileEntity();
			profile.setAccountFullName("System Demo Account");
			profile.setAccountDateOfBirth(LocalDate.of(2000, 1, 1));
			profile.setAccountParent(account);
			
			profileRepository.save(profile);
		}
	}
}
