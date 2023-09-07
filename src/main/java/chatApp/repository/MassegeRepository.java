package chatApp.repository;

import chatApp.Entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MassegeRepository extends JpaRepository<ChatMessage, String> {
}