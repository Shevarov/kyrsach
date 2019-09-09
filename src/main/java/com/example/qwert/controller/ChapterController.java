package com.example.qwert.controller;

import com.example.qwert.domain.Chapter;
import com.example.qwert.domain.Composition;
import com.example.qwert.domain.User;
import com.example.qwert.repos.ChapterRepository;
import com.example.qwert.repos.CompositionRepository;
import com.example.qwert.service.AmazonClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ChapterController {


    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private AmazonClient amazonClient;

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
                              @RequestParam String text,
                              @RequestParam(value = "chapterId",defaultValue = "-1") String chapterId,
                              @RequestParam("fileName")MultipartFile file, @RequestParam(value = "checkboxes", defaultValue = "") String pressedCheckbox,Model model
                              ) {
        if(StringUtils.isBlank(text) || StringUtils.isBlank(chapterName)){
            model.addAttribute("user",user);
            model.addAttribute("composition",composition);
            if(chapterId.equals("-1"))
            model.addAttribute("chapter",new Chapter(chapterName,text));
            else  model.addAttribute("chapter",chapterRepository.findChapterById(Integer.parseInt(chapterId)));
            return "chapter";
        }
            Chapter chapter;
            if (!chapterId.equals("-1")) {
                chapter = chapterRepository.findChapterById(Integer.parseInt(chapterId));
                chapter.setChapterName(chapterName);
                chapter.setText(text);
                if (file != null && !file.getOriginalFilename().isEmpty() && pressedCheckbox.equals("")) {
                    if (chapter.getFileName() != null)
                        amazonClient.deleteFileFroms3bucket(chapter.getFileName());
                    String fileName = amazonClient.upload(file);
                    chapter.setFileName(fileName);
                }
                if (!pressedCheckbox.equals("")) {
                    amazonClient.deleteFileFroms3bucket(chapter.getFileName());
                    chapter.setFileName(null);
                }
                chapterRepository.save(chapter);
                return "redirect:/composition/" + user.getId() + "/" + composition.getId();
            } else {
                if (file != null && !file.getOriginalFilename().isEmpty()) {
                    String fileName = amazonClient.upload(file);
                    chapter = new Chapter(chapterName, text, fileName, composition);
                } else chapter = new Chapter(chapterName, text, null, composition);
                chapterRepository.save(chapter);
                composition.getChapter().add(chapter);
                compositionRepository.save(composition);
            }
        return "redirect:/composition/" + user.getId() + "/" + composition.getId();
       // return "redirect:/users/" + user.getId();
    }

}