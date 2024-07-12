package uz.pdp.cvcreator.component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.cvcreator.entity.Skill;
import uz.pdp.cvcreator.repo.SkillRepo;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final SkillRepo skillRepo;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (ddl.equals("create")){
            List<Skill> skills = new ArrayList<>(List.of(
                    new Skill(1, "Java"),
                    new Skill(2, "Java Script"),
                    new Skill(3, "HTML"),
                    new Skill(4, "Spring"),
                    new Skill(5, "React"),
                    new Skill(6, "PostgreSQL")
            ));
            skillRepo.saveAll(skills);
        }
    }
}
