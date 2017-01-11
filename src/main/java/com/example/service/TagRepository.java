package com.example.service;

/**
 * Created by pengg on 5/31/2016.
 */

import com.example.domain.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by pengg on 5/31/2016.
 */
public interface TagRepository extends CrudRepository<Tag, Long> {
  Tag findByName(String name);

  @Query("select t from Tag t where t.name like ?1 order by t.name")
  List<Tag> searchByTerm(String term);

}