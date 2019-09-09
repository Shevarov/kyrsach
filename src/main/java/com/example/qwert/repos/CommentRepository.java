package com.example.qwert.repos;

import com.example.qwert.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

 //   Iterable<Comment> findByComposition_Id(Integer id);
    List<Comment> findByComposition_Id(Integer id);
}
