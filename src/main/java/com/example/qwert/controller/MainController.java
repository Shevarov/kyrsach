package com.example.qwert.controller;

import com.example.qwert.domain.Chapter;
import com.example.qwert.domain.Comment;
import com.example.qwert.domain.Composition;
import com.example.qwert.domain.User;
import com.example.qwert.repos.ChapterRepository;
import com.example.qwert.repos.CommentRepository;
import com.example.qwert.repos.CompositionRepository;
import com.example.qwert.repos.UserRepository;
import com.example.qwert.service.AmazonClient;
import com.example.qwert.service.CompositionSearchService;
import com.example.qwert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CompositionSearchService searchService;

    @GetMapping("/")
    public String greeting(Model model) {
        List<Composition> compositions = compositionRepository.findAll(Sort.by("publicationDate"));
        int size=0;
        if(compositions.size()>10){
            size=compositions.size()-10;
        }else if(compositions.size()>5)
            size=compositions.size()-5;
        else if(compositions.size()>1)
            size=compositions.size()-1;
        List<Composition> compositions1=new ArrayList<>();
        for(int a=compositions.size()-1;a>=size;a--){
            compositions1.add(compositions.get(a));
            model.addAttribute("listOfCompositions",compositions1);
        }
        return "main";
    }

    @GetMapping("/searchText")
    public String findComposition(@RequestParam(name = "searchText") String searchText,Model model)  {
        if(!searchText.equals("")){
        List<Composition> compositions = null;
        try {if(!searchText.equals(""))
            compositions = searchService.findComposition(searchText);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(compositions.size()>0)
        model.addAttribute("listOfCompositions",compositions);}
        return "resultOfFind";
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
                            @RequestParam(name = "submit", defaultValue = "") String buttonName,
                                      @RequestParam(defaultValue = "") String password,@RequestParam(defaultValue = "") String email)
    {
        if(buttonName.equals("Save")){
            userService.updateUser(user,password,email);
        }

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
                List<Chapter> chapters= chapterRepository.findChapterByComposition_Id(Integer.parseInt(pressedCheckbox));
                for (Chapter chapter : chapters) {
                    if (chapter.getFileName() != null)
                        amazonClient.deleteFileFroms3bucket(chapter.getFileName());
                    chapterRepository.deleteById(chapter.getId());
                }
                Iterable<Comment> comments= commentRepository.findByComposition_Id(Integer.parseInt(pressedCheckbox));
                for (Comment comment:comments){
                    commentRepository.deleteById(comment.getId());
                }
                compositionRepository.deleteById(Integer.parseInt(pressedCheckbox));
            }
        }
        userRepository.save(user);
        return "redirect:/users/"+user.getId();
    }

}