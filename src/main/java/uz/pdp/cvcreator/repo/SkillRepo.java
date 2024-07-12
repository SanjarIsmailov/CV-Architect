package uz.pdp.cvcreator.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cvcreator.entity.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepo extends JpaRepository<Skill, Integer> {
    Skill findByName(String name);
    List<Skill> findByNameContainingIgnoreCase(String name);
}
