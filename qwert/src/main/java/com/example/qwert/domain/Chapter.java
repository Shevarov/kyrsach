package com.example.qwert.domain;

import javax.persistence.*;

@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String chapterName;
    private String text;
    private String fileName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "composition_id")
    private Composition composition;

    public Chapter(String chapterName, String text, String fileName, Composition composition) {
        this.chapterName = chapterName;
        this.text = text;
        this.fileName = fileName;
        this.composition = composition;
    }

    public Chapter() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }
}
