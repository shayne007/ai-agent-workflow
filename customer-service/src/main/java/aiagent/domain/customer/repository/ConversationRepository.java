package aiagent.domain.customer.repository;

import aiagent.domain.customer.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    
    // Change the method signature to use Instant instead of LocalDateTime
    // to match the field type in the Conversation entity
    List<Conversation> findByLastActivityTimeBefore(Instant timestamp);
    
    // Add other query methods as needed
}