package br.com.arthub.ah_rest_useraccount.api.v1.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_account_profiles")
@Data
public class UserAccountProfileEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "profile_id", nullable = false)
	private UUID profileId;
	
	@Column(name = "account_full_name", nullable = false)
	private String accountFullName;
	
	@Column(name = "account_date_of_birth", nullable = false)
	private LocalDate accountDateOfBirth;
	
	@OneToOne
	@JoinColumn(name = "account_fk")
	private UserAccountEntity accountParent;
}
