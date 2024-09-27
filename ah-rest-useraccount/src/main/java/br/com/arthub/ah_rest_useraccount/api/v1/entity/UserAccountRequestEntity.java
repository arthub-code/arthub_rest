package br.com.arthub.ah_rest_useraccount.api.v1.entity;

import java.util.UUID;

import br.com.arthub.ah_rest_useraccount.api.v1.constants.UserAccountType;
import br.com.arthub.ah_rest_useraccount.api.v1.dto.CreateAnAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_accounts_request")
@Data
public class UserAccountRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID userAccountId;
	
	@Column(name = "account_username", unique = true, nullable = false)
	private String accountUsername;
	
	@Column(name = "account_social_name", nullable = false)
	private String accountSocialName;
	
	@Column(name = "account_email", unique = true ,nullable = false)
	private String accountEmail;
	
	@Column(name = "account_password", nullable = false)
	private String accountPassword;
	
	@Column(name = "user_account_type")
	@Enumerated(EnumType.STRING)
	private UserAccountType userAccountType;

	@Lob
	@Column(unique = true)
	private String token;

    public UserAccountRequestEntity() {

    }

    public UserAccountRequestEntity(CreateAnAccount dto, String token) {
        this.accountEmail = dto.getEmail();
        this.accountPassword = dto.getPassword();
        this.accountSocialName = dto.getSocialName();
        this.accountUsername = dto.getUsername();
        this.userAccountType = dto.getUserAccountType();
		this.token = token;
    }
}
