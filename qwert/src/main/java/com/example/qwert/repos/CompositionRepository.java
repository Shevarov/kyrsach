package com.example.qwert.repos;

import com.example.qwert.domain.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition,Integer> {

    List<Composition> findByAuthor_Id(Integer author_id);
    Composition findCompositionById(Integer id);
}
