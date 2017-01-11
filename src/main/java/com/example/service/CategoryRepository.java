package com.example.service;
import java.util.List;

import com.example.domain.Category;
import org.springframework.data.repository.CrudRepository;
/**
 * Created by pengg on 5/31/2016.
 */
public interface CategoryRepository extends CrudRepository<Category, Long>  {
  List<Category> findByName(String name);
  Category findById(Long id);
}
