
package uz.pdp.cvcreator.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uz.pdp.cvcreator.entity.Education;
import uz.pdp.cvcreator.entity.Experience;
import uz.pdp.cvcreator.entity.Skill;
import uz.pdp.cvcreator.entity.User;
import uz.pdp.cvcreator.repo.SkillRepo;
import uz.pdp.cvcreator.repo.UserRepo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cv")
public class CvController {

    private final UserRepo userRepository;
    private final SkillRepo skillRepository;

    @GetMapping("/create")
    public String showCreateCvForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("skills", skillRepository.findAll());
        return "home";
    }

    @PostMapping("/save")
    public void saveCv(@ModelAttribute User user, HttpServletResponse response) throws IOException {
        List<Skill> skills = user.getSkills().stream()
                .map(skill -> skillRepository.findByName(skill.getName()) != null
                        ? skillRepository.findByName(skill.getName())
                        : skillRepository.save(new Skill(skill.getId(),skill.getName())))
                .toList();

        user.setSkills(skills);
        userRepository.save(user);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 750);
        contentStream.showText("CV");
        contentStream.newLine();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText("First Name: " + user.getFirstName());
        contentStream.newLine();
        contentStream.showText("Last Name: " + user.getLastName());
        contentStream.newLine();
        contentStream.showText("Email: " + user.getEmail());
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText("Education:");
        contentStream.newLine();
        for (Education education : user.getEducations()) {
            contentStream.showText(education.getName() + " (" + education.getStartYear() + " - " + education.getFinishYear() + ")");
            contentStream.newLine();
        }
        contentStream.newLine();
        contentStream.showText("Experience:");
        contentStream.newLine();
        for (Experience experience : user.getExperiences()) {
            contentStream.showText(experience.getCompany() + " (" + experience.getStartYear() + " - " + experience.getFinishYear() + ")");
            contentStream.newLine();
        }
        contentStream.newLine();
        contentStream.showText("Skills:");
        contentStream.newLine();
        for (Skill skill : user.getSkills()) {
            contentStream.showText(skill.getName());
            contentStream.newLine();
        }
        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        document.save(output);
        document.close();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=cv.pdf");
        response.setContentLength(output.size());

        try (OutputStream outStream = response.getOutputStream()) {
            outStream.write(output.toByteArray());
        }
    }


    @GetMapping("/skills/search")
    @ResponseBody
    public List<Skill> searchSkills(@RequestParam("q") String query) {
        return skillRepository.findByNameContainingIgnoreCase(query);
    }
}