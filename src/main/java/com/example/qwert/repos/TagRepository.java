package com.example.qwert.repos;

import com.example.qwert.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByTagName(String tagName);
}
