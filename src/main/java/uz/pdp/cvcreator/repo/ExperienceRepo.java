package uz.pdp.cvcreator.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cvcreator.entity.Experience;

public interface ExperienceRepo extends JpaRepository<Experience, Integer> {
}
