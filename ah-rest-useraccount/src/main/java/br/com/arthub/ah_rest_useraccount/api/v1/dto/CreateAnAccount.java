package br.com.arthub.ah_rest_useraccount.api.v1.dto;

import java.time.LocalDate;

import br.com.arthub.ah_rest_useraccount.api.v1.constants.UserAccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAnAccount {
	private String email;
	private String socialName;
	private String username;
	private String password;
	private UserAccountType userAccountType;
	private String fullName;
	private LocalDate dateOfBirth;
}
