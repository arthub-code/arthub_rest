package br.com.arthub.ah_rest_useraccount.api.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_useraccount.api.v1.dto.CreateAnAccount;
import br.com.arthub.ah_rest_useraccount.api.v1.entity.UserAccountRequestEntity;
import br.com.arthub.ah_rest_useraccount.api.v1.repository.UserAccountRequestRepository;
import br.com.arthub.ah_rest_useraccount.api.v1.utils.JwtUtils;

@Service
public class UserAccountRequestService {
    @Autowired
    private UserAccountRequestRepository repository;
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * @param createDto
     * 
     * <p>Salva os dados de cadastro na tabela de requisição de conta.</p>
     * <p><strong>Atenção!</strong> Esse método não cuida de validações, por isso não retorna nada.</p>
    */
    public void saveUserAccountData(CreateAnAccount createDto, String token) {
        repository.saveAndFlush(new UserAccountRequestEntity(createDto, token));
    }

    public UserAccountRequestEntity findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void delete(UserAccountRequestEntity entity) {
        this.repository.delete(entity);
    }

    public void clearExpiredDatas() {
        for(UserAccountRequestEntity data : this.repository.findAll()) {
            if(jwtUtils.isTokenExpired(data.getToken())) {
                this.repository.delete(data);
            }
        }
    }
}
