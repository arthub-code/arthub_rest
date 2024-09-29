package br.com.arthub.ah_rest_email.dto;

import br.com.arthub.ah_rest_email.constants.EmailOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String body;
    private String token;
    private String confirmEndpoint;
    private String to;
    private String subject;
    private EmailOrder emailOrder;
    private String username;
}
