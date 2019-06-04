package com.example.qwert.domain;

import javax.persistence.*;

@Entity
public class Composition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String shortDescription;
    private String tag;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Composition() {
    }

    public Composition(String title, String shortDescription, String tag) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.tag = tag;
    }

    public Composition(String title, String shortDescription, String tag, User author) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.tag = tag;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
