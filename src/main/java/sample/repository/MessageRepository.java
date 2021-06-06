package sample.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import sample.entity.OutputMessage;
import sample.entity.Role;

public interface MessageRepository extends JpaRepository<OutputMessage, Long> {
}
