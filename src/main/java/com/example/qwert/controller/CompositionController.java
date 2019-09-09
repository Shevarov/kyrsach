package com.example.qwert.controller;

import com.example.qwert.domain.Chapter;
import com.example.qwert.domain.Composition;
import com.example.qwert.domain.Tag;
import com.example.qwert.domain.User;
import com.example.qwert.repos.ChapterRepository;
import com.example.qwert.repos.CompositionRepository;
import com.example.qwert.repos.TagRepository;
import com.example.qwert.service.AmazonClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class CompositionController {

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private TagRepository tagRepository;


    @GetMapping("/composition/{user}")
    public String createComposition(@PathVariable User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("composition",new Composition());
        return "composition";
    }

    @GetMapping("/composition/{user}/{idComposition}")
    public String redactComposition(@PathVariable User user,@PathVariable Composition idComposition, Model model,@AuthenticationPrincipal User user1){
        if(idComposition==null || user==null)
            return "redirect:/users/"+user1.getId();
        model.addAttribute("user",user);
        model.addAttribute("composition",idComposition);
        Iterable<Chapter> chapters= chapterRepository.findChapterByComposition_Id(idComposition.getId());
        if(((List<Chapter>) chapters).size()!=0)
        model.addAttribute("listOfChapters",chapters);
        return "composition";
    }

    @PostMapping("/composition")
    public String saveComposition(@RequestParam("userId") User user, @RequestParam String title,
                                  @RequestParam String genre, @RequestParam String shortDescription,
                                  @RequestParam(defaultValue = "") String tagsName[], @RequestParam(value = "compositionId",defaultValue = "-1") String compositionId,
                                  @RequestParam(name = "submit", defaultValue = "") String buttonName,@RequestParam String chapterId){

        if(StringUtils.isBlank(title)||StringUtils.isBlank(genre)||StringUtils.isBlank(shortDescription) || tagsName.length==0){
            if(compositionId.equals("-1"))
                return "redirect:/composition/"+user.getId();
            else return "redirect:/composition/"+user.getId()+"/"+compositionId;
        }
        if(!chapterId.equals("")){
            Chapter chapter=chapterRepository.findChapterById(Integer.parseInt(chapterId));
                if (chapter.getFileName() != null)
                    amazonClient.deleteFileFroms3bucket(chapter.getFileName());
            Composition composition;
            composition = compositionRepository.findCompositionById(Integer.parseInt(compositionId));
            composition.getTages().remove(chapter);
            chapterRepository.deleteById(Integer.parseInt(chapterId));
            return "redirect:/composition/" + user.getId()+"/"+compositionId;
        }
        if(buttonName.equals("Save")) {
            Composition composition;
            if (!compositionId.equals("-1")) {
                composition = compositionRepository.findCompositionById(Integer.parseInt(compositionId));
                composition.setTitle(title);
                composition.setGenre(genre);
                composition.setShortDescription(shortDescription);
                composition.getTages().clear();
                addTag(tagsName, composition);
                composition.setPublicationDate(new Date());
                compositionRepository.save(composition);
                return "redirect:/composition/" + user.getId()+"/"+compositionId;
            } else {
                composition = new Composition(title, genre, shortDescription, user);
                composition.setPublicationDate(new Date());
                addTag(tagsName, composition);
            }
            compositionRepository.save(composition);
            return "redirect:/chapter/" + user.getId() + "/" + composition.getId();
        }
        return "redirect:/chapter/" + user.getId() + "/" + compositionId;
    }

    private void addTag(@RequestParam(defaultValue = "") String[] tagsName, Composition composition) {
        for (String tagName : tagsName) {
            Tag tag = tagRepository.findByTagName(tagName);
            if (tag != null)
                composition.getTages().add(tag);
            else composition.getTages().add(new Tag(tagName));
        }
    }

    @PostMapping("/getTags")
    public void jasona(HttpServletResponse response) throws IOException {
        List<Tag> tags = tagRepository.findAll();
        new ObjectMapper(). writeValue (response.getOutputStream(), tags);
    }

}