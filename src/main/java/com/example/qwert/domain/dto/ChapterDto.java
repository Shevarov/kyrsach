package com.example.qwert.domain.dto;

import com.example.qwert.domain.Chapter;
import com.example.qwert.domain.Composition;

public class ChapterDto {
    private Integer id;
    private String chapterName;
    private String text;
    private String fileName;
    private Composition composition;
    private Long likes;
    private Boolean meLiked;


    public ChapterDto(Chapter chapter,Long likes, Boolean meLiked) {
        this.id=chapter.getId();
        this.chapterName=chapter.getChapterName();
        this.text=chapter.getText();
        this.fileName=chapter.getFileName();
        this.composition=chapter.getComposition();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public Integer getId() {
        return id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public String getText() {
        return text;
    }

    public String getFileName() {
        return fileName;
    }

    public Composition getComposition() {
        return composition;
    }

    public Long getLikes() {
        return likes;
    }

    public Boolean getMeLiked() {
        return meLiked;
    }


}
