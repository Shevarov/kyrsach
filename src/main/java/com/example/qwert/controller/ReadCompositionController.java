package com.example.qwert.controller;

import com.example.qwert.domain.Chapter;
import com.example.qwert.domain.Comment;
import com.example.qwert.domain.Composition;
import com.example.qwert.domain.User;
import com.example.qwert.domain.dto.ChapterDto;
import com.example.qwert.repos.*;
import com.example.qwert.service.AmazonClient;
import com.example.qwert.service.CompositionSearchService;
import com.example.qwert.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
public class ReadCompositionController {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/readComposition/{chapter}/like")
    public String like(@AuthenticationPrincipal User user, @PathVariable Chapter chapter,
                       RedirectAttributes redirectAttributes, @RequestHeader(required = false) String referer) {

        Set<User> likes = chapter.getLikes();
        boolean isEquals = false;
        Iterator<User> iterator = likes.iterator();
        while (iterator.hasNext()) {
            User like = iterator.next();
            if (like.getId().equals(user.getId())) {
                iterator.remove();
                isEquals = true;
            }
        }
        if (!isEquals)
            likes.add(user);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }

    @GetMapping("/readComposition/{idComposition}")
    public String readComposition(@AuthenticationPrincipal User user,@PathVariable Composition idComposition,Model model){
        if(idComposition==null)
            return "redirect:/";
        model.addAttribute("composition",idComposition);
        Iterable<ChapterDto> chapters= chapterRepository.findByComposition_Id(idComposition.getId(),user);
        model.addAttribute("listOfChapters",chapters);
        Iterable<Comment> comments = commentRepository.findByComposition_Id(idComposition.getId());
        model.addAttribute("sizeOfComments",((List<Comment>) comments).size());
        model.addAttribute("listOfComments",comments);
        return "readComposition";
    }

    @PostMapping("/readComposition")
    public String addComment(@RequestParam(name = "submit", defaultValue = "") String buttonName,
                               @AuthenticationPrincipal User user,
                               @RequestParam("compositionId") Composition composition,@Valid Comment comment,
                               BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            Map<String,String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("comment",comment);
        } else {
            comment.setAuthor(user);
            comment.setComposition(composition);
            model.addAttribute("comment",null);
            if (buttonName.equals("Add comment")) {
                commentRepository.save(comment);
                composition.getComments().add(comment);
            }
        }
        if(composition==null)
            return "redirect:/";
        model.addAttribute("composition",composition);
        Iterable<ChapterDto> chapters= chapterRepository.findByComposition_Id(composition.getId(),user);
        model.addAttribute("listOfChapters",chapters);
        Iterable<Comment> comments = commentRepository.findByComposition_Id(composition.getId());
        model.addAttribute("sizeOfComments",((List<Comment>) comments).size());
        model.addAttribute("listOfComments",comments);
        return "readComposition";
        //return "redirect:/readComposition/"+composition.getId();
    }

    @PostMapping("/update/comment")
    public void updateComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String compositionId = req.getParameter("compositionId");
        int sizeOfComments = Integer.parseInt(req.getParameter("sizeOfComments"));
        List<Comment> comments = commentRepository.findByComposition_Id(Integer.parseInt(compositionId));
        if(comments.size()>sizeOfComments) {
            List<Comment> comments1 = new ArrayList<>();
            for(int a=sizeOfComments;a<comments.size();a++){
                comments1.add(comments.get(a));
            }
            new ObjectMapper(). writeValue (resp.getOutputStream(), comments1);
        }
    }

}