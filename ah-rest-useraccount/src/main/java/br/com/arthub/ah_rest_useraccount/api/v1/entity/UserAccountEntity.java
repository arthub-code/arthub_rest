package br.com.arthub.ah_rest_useraccount.api.v1.entity;

import java.time.LocalDate;
import java.time.LocalTime;
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
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_accounts")
@Data
public class UserAccountEntity {
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
	
	@Column(name = "creation_date")
	private LocalDate creationDate;
	@Column(name = "creation_time")
	private LocalTime creationTime;
	
	@Column(name = "is_account_active")
	private boolean isAccountActive;
	@Column(name = "is_account_suspended")
	private boolean isAccountSuspended;
	
	public UserAccountEntity() {
		
	}

	/**
	 * @param dto
	 * 
	 * <p>Atributos que não são salvos no objeto pelo construtor:</p>
	 * <ul>
	 * 	<li><strong>Username</strong></li>
	 * 	<li><strong>Password</strong></li>
	 * </ul>
	 * 
	 * <p><strong>Atenção!</strong> Você deve salvar esses dados manualmente pelos métodos <code>setters</code> da classe.</p>
	 * */
	public UserAccountEntity(CreateAnAccount dto) {
		this.accountEmail = dto.getEmail();
		this.accountSocialName = dto.getSocialName();
		this.accountEmail = dto.getEmail();
		this.userAccountType = dto.getUserAccountType();
		startNewAccount();
	}
	
	private void startNewAccount() {
		this.isAccountActive = true;
		this.isAccountSuspended = false;
		this.creationDate = LocalDate.now();
		this.creationTime = LocalTime.now();
	}
	
}
