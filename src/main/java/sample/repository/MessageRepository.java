package sample.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sample.entity.OutputMessage;

public interface MessageRepository extends JpaRepository<OutputMessage, Long> {
}
