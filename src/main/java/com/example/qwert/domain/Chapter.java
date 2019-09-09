package com.example.qwert.domain;

import org.hibernate.search.annotations.Field;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Field
    private String chapterName;
    @Field
    @Size(max = 10000)
    private String text;

    private String fileName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "composition_id")
    private Composition composition;

    @ManyToMany()
    @JoinTable(
            name = "chapter_likes",
            joinColumns = @JoinColumn(name = "chapter_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likes = new HashSet<>();

    public Chapter(String chapterName, String text, String fileName, Composition composition) {
        this.chapterName = chapterName;
        this.text = text;
        this.fileName = fileName;
        this.composition = composition;
    }

    public Chapter(String chapterName, @Size(max = 10000) String text) {
        this.chapterName = chapterName;
        this.text = text;
        id=-1;
        fileName=null;
        composition=null;
    }

    public Chapter() {
        id=-1;
        chapterName="";
        text="";
        fileName=null;
        composition=null;

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

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }
}
