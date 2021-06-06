package sample.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import sample.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
