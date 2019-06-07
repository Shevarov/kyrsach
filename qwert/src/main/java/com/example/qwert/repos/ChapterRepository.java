package com.example.qwert.repos;

import com.example.qwert.domain.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
    List<Chapter> findByComposition_Id(Integer composition_id);
    Chapter findChapterById(Integer id);
}
