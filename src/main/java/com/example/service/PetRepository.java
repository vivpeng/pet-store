package com.example.service;

/**
 * Created by pengg on 5/31/2016.
 */

import com.example.domain.Pet;
import com.example.domain.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

/**
 * Created by pengg on 5/31/2016.
 */
@Transactional
public interface PetRepository extends CrudRepository<Pet, Long> {
  List<Pet> findAllByOrderByIdDesc();
  List<Pet> findByName(String name);
  @Query("select p from Pet p inner join p.tags t where t.name = ?1")
  HashSet<Pet> searchByTagName(String tag);
  Pet findById(Long id);

}