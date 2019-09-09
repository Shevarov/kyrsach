package com.example.qwert.repos;

import com.example.qwert.domain.Chapter;
import com.example.qwert.domain.User;
import com.example.qwert.domain.dto.ChapterDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter,Integer> {

    @Query(
           "select new com.example.qwert.domain.dto.ChapterDto("+
           "  c,"+"count (ci),"+"(sum (case when ci= :user then 1 else 0 end ) > 0))"+
                   "from Chapter c left join c.likes ci " +
                   "where (c.composition.id =:composition_id)" +
                   "group by c"
    )
    List<ChapterDto> findByComposition_Id(@Param("composition_id")Integer composition_id, @Param("user")User user);
    Chapter findChapterById(Integer id);
    List<Chapter> findChapterByComposition_Id(Integer composition_id);
}
