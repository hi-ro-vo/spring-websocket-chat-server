package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
