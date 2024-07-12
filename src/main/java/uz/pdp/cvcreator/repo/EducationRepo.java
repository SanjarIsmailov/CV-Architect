package uz.pdp.cvcreator.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cvcreator.entity.Education;

public interface EducationRepo extends JpaRepository<Education, Integer> {
}
