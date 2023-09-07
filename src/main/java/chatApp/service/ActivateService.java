package chatApp.service;

import chatApp.Entities.ActiveUser;
import chatApp.repository.ActivateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivateService {

    @Autowired
    ActivateRepository activateRepository;

    /**
     * The function get activateUser type, that want to register,
     * and keep his details on DB until he will activate his email
     * @param response The user details with the generate code.
     * @return true if every thing success.
     */
    public boolean keepOnDB(ActiveUser response) {
        if (response != null) {
            if(activateRepository.findByEmail(response.getEmail())!=null)
                throw new IllegalArgumentException(String.format("You Are Already Registered!! Please Activate Your Account On Email!"));

            activateRepository.save(response);
            return true;
        }

        return false;
    }
}