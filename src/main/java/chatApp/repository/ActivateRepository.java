package chatApp.repository;


import chatApp.Entities.ActiveUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivateRepository extends JpaRepository<ActiveUser, String> {

    ActiveUser findByEmail(String email);

    ActiveUser findByCode(String code);
}