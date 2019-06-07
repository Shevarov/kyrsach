package com.example.qwert.controller;

import com.example.qwert.domain.Chapter;
import com.example.qwert.domain.Composition;
import com.example.qwert.domain.Role;
import com.example.qwert.domain.User;
import com.example.qwert.repos.ChapterRepository;
import com.example.qwert.repos.CompositionRepository;
import com.example.qwert.repos.UserRepository;
import com.example.qwert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String greeting() {

        return "greeting";
    }

    @GetMapping("/readComposition/{idComposition}")
    public String readComposition(Model model, @PathVariable Composition idComposition){
        if(idComposition==null)
            return "redirect:/";
        model.addAttribute("composition",idComposition);
        Iterable<Chapter> chapters= chapterRepository.findByComposition_Id(idComposition.getId());
        model.addAttribute("listOfChapters",chapters);
        return "readComposition";
    }

    @GetMapping("/chapter/{user}/{idComposition}")
    public String createChapter(@PathVariable User user,@PathVariable Composition idComposition, Model model){
        model.addAttribute("user",user);
        model.addAttribute("chapter",new Chapter());
        model.addAttribute("composition",idComposition);
        return "chapter";
    }

    @GetMapping("/chapter/{user}/{idComposition}/{idChapter}")
    public String redactChapter(@PathVariable User user,@PathVariable Composition idComposition, Model model,@PathVariable Chapter idChapter){
        if(idChapter==null || idComposition==null)
            return "redirect:/users/"+user.getId();
        model.addAttribute("user",user);
        model.addAttribute("composition",idComposition);
        model.addAttribute("chapter",idChapter);
        return "chapter";
    }

    @PostMapping("/chapter")
    public String saveChapter(@RequestParam("userId") User user, @RequestParam("compositionId") Composition composition,
                              @RequestParam String chapterName,
                              @RequestParam String text, @RequestParam(value = "chapterId",defaultValue = "-1") String chapterId,
                              @RequestParam("fileName")MultipartFile file) throws IOException {
        Chapter chapter;
        if (!chapterId.equals("-1")) {
            chapter = chapterRepository.findChapterById(Integer.parseInt(chapterId));
            chapter.setChapterName(chapterName);
            chapter.setText(text);
            if(file!=null && !file.getOriginalFilename().isEmpty()){
                File file1 = new File(uploadPath);
                if(!file1.exists())
                    file1.mkdir();
              String uuidFile=  UUID.randomUUID().toString();
              String resultFile=uuidFile+"."+file.getOriginalFilename();
              file.transferTo(new File(uploadPath+"/"+resultFile));
                chapter.setFileName(resultFile);
            }
            chapterRepository.save(chapter);
            return "redirect:/composition/" + user.getId()+"/"+composition.getId();
        } else{
            if(file!=null && !file.getOriginalFilename().isEmpty()){
                File file1 = new File(uploadPath);
                if(!file1.exists())
                    file1.mkdir();
                String uuidFile=  UUID.randomUUID().toString();
                String resultFile=uuidFile+"."+file.getOriginalFilename();
                file.transferTo(new File(uploadPath+"/"+resultFile));
                chapter = new Chapter(chapterName, text, resultFile, composition);
            }
            else chapter = new Chapter(chapterName, text, null, composition);
        chapterRepository.save(chapter);}

        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/composition/{user}")
    public String createComposition(@PathVariable User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("composition",new Composition());
        return "composition";
    }

    @GetMapping("/composition/{user}/{idComposition}")
    public String redactComposition(@PathVariable User user,@PathVariable Composition idComposition, Model model){
        if(idComposition==null)
            return "redirect:/users/"+user.getId();
        model.addAttribute("user",user);
        model.addAttribute("composition",idComposition);
        Iterable<Chapter> chapters= chapterRepository.findByComposition_Id(idComposition.getId());
        model.addAttribute("listOfChapters",chapters);
        return "composition";
    }

    @PostMapping("/composition")
    public String saveComposition(@RequestParam("userId") User user, @RequestParam String title,
                                  @RequestParam String genre, @RequestParam String shortDescription,
                                  @RequestParam String tags, @RequestParam(value = "compositionId",defaultValue = "-1") String compositionId,
                                  @RequestParam(name = "submit", defaultValue = "") String buttonName){
        if(buttonName.equals("Save")) {
            Composition composition;
            if (!compositionId.equals("-1")) {
                composition = compositionRepository.findCompositionById(Integer.parseInt(compositionId));
                composition.setTitle(title);
                composition.setGenre(genre);
                composition.setShortDescription(shortDescription);
                composition.setTag(tags);
                compositionRepository.save(composition);
                return "redirect:/composition/" + user.getId()+"/"+compositionId;
            } else
                composition = new Composition(title, genre, shortDescription, tags, user);
            compositionRepository.save(composition);
            return "redirect:/chapter/" + user.getId() + "/" + composition.getId();
        }
        return "redirect:/chapter/" + user.getId() + "/" + compositionId;
    }









    @GetMapping("/users/{user}")
    public String personalPage(@PathVariable User user, Model model,Principal principal){
        String nameUser = principal.getName();
        User user1 = userRepository.findByUsername(nameUser);
        if(user==null || (!user1.isAdmin() && user.getId()!=user1.getId())){
            return "redirect:/users/"+user1.getId();}
        model.addAttribute("user",user);
        Iterable<Composition> compositions= compositionRepository.findByAuthor_Id(user.getId());
        model.addAttribute("listOfCompositions",compositions);
        return "personalPage";
    }

    @PostMapping("/personalPage")
    public String editOnPersonalPage( @RequestParam("userId") User user,
                            @RequestParam(value = "checkboxes", defaultValue = "") String pressedCheckbox, Model model,
                            @RequestParam(name = "submit", defaultValue = "") String buttonName)
    {
        if (buttonName.equals("Create"))
        {
            return "redirect:/composition/"+user.getId();
        }
        if (buttonName.equals("Redaction"))
        {
            if(!pressedCheckbox.equals("")){
                return "redirect:/composition/"+user.getId()+"/"+pressedCheckbox;
            }
        }
        if (buttonName.equals("Open for read"))
        {
            if (!pressedCheckbox.equals("")) {
                return "redirect:/readComposition/"+pressedCheckbox;
            }
        }
        if (buttonName.equals("Delete"))
        {
            if(!pressedCheckbox.equals("")){
                List<Chapter> chapters= chapterRepository.findByComposition_Id(Integer.parseInt(pressedCheckbox));
                for (Chapter chapter : chapters) {
                    chapterRepository.deleteById(chapter.getId());
                }
                compositionRepository.deleteById(Integer.parseInt(pressedCheckbox));
            }
        }
        userRepository.save(user);
        return "redirect:/users/"+user.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String users(Model model) {
        Iterable<User> messages = userRepository.findAll();
        model.addAttribute("listOfUsers", messages);
        model.addAttribute("role",Role.ADMIN);
        return "users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users")
    public String getInputText(@RequestParam(value = "checkboxes", defaultValue = "") String[] pressedCheckbox, Model model,
                               @RequestParam(name = "submit", defaultValue = "") String buttonName, Principal principal
    ) {
        String nameUser = principal.getName();
        User user1 = userRepository.findByUsername(nameUser);
        if (buttonName.equals("Block")) {
            for (String aColumn1 : pressedCheckbox) {
                User user = userRepository.findUserById(Integer.parseInt(aColumn1));
                user.setStatus("Block");
                userRepository.save(user);
            }
            for (String aColumn1 : pressedCheckbox)
                if (user1.getId() == Integer.parseInt(aColumn1))
                    return "redirect:/logout";
        }
        if (buttonName.equals("Unlock"))
            for (String aColumn1 : pressedCheckbox) {
                User user = userRepository.findUserById(Integer.parseInt(aColumn1));
                user.setStatus("Unlock");
                userRepository.save(user);
            }
        if (buttonName.equals("Delete")) {
            for (String aColumn1 : pressedCheckbox)
                userRepository.deleteById(Integer.parseInt(aColumn1));
            for (String aColumn1 : pressedCheckbox)
                if (user1.getId() == Integer.parseInt(aColumn1))
                    return "redirect:/logout";
        }
        Iterable<User> messages = userRepository.findAll();
        model.addAttribute("listOfUsers", messages);
        model.addAttribute("role",Role.ADMIN);
        return "users";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        String answer = userService.addUser(user);
        if(answer.equals("Exists")){
            model.put("message", "User exists!");
            return "registration";
        }
        if (answer.equals("Error")) {
            model.put("message", "Error");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model,@PathVariable String code){
        boolean isActivated = userService.activateUser(code);
        if(isActivated){
            model.addAttribute("message","User activated");
        }
        else model.addAttribute("message","Activation code is not found");
        return "login";
    }

    @PostMapping("/test/post")
    public String changeCheckboxes(HttpServletRequest req, Principal principal) {
        String nameUser = principal.getName();
        User user1 = userRepository.findByUsername(nameUser);
        String checked = req.getParameter("checked");
        String id = req.getParameter("id");
        User user = userRepository.findUserById(Integer.parseInt(id));
        if(checked.equals("true")){
            user.getRoles().add(Role.ADMIN);
        }
        else{
            user.getRoles().remove(Role.ADMIN);
        }
        userRepository.save(user);
//        if(user1.getId()==Integer.parseInt(id))
//            return "redirect:/logout";
        return "redirect:/users";
    }
}