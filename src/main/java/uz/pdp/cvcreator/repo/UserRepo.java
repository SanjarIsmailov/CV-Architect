package uz.pdp.cvcreator.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cvcreator.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
}
