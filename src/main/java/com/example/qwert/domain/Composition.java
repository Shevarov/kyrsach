package com.example.qwert.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Indexed
public class Composition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
 //   @NotBlank(message = "Please write title")
    @Field
    private String title;
    @Field
    private String genre;
  //  @NotBlank(message = "Please write short description")
    @Size(max = 400)
    @Field
    private String shortDescription;
 //   @NotBlank(message = "Tag can not be empty")
    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @IndexedEmbedded
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "composition_tag",
            joinColumns = @JoinColumn(name = "composition_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tages = new HashSet<>();

    @IndexedEmbedded
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "composition_id1")
    private Set<Chapter> chapter=new HashSet<>();

    @IndexedEmbedded
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "composition_id1")
    @JsonManagedReference
    private Set<Comment> comments=new HashSet<>();

    private Date publicationDate;

    public Composition() {
        id = -1;
        title="";
        genre="";
        shortDescription=null;
    }

    public Composition(String title, String genre, @Size(max = 400) String shortDescription, User author) {
        this.title = title;
        this.genre = genre;
        this.shortDescription = shortDescription;
        this.author = author;
    }

    public Composition(String title, String genre, @Size(max = 400) String shortDescription, User author, Set<Tag> tages, Set<Chapter> chapter, Set<Comment> comments) {
        this.title = title;
        this.genre = genre;
        this.shortDescription = shortDescription;
        this.author = author;
        this.tages = tages;
        this.chapter = chapter;
        this.comments = comments;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Tag> getTages() {
        return tages;
    }

    public void setTages(Set<Tag> tages) {
        this.tages = tages;
    }

    public Set<Chapter> getChapter() {
        return chapter;
    }

    public void setChapter(Set<Chapter> chapter) {
        this.chapter = chapter;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
}
