package com.example.service;

import com.example.domain.Pet;
import com.example.domain.PhotoUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by pengg on 6/7/2016.
 */

@Transactional
public interface PhotoRepository extends CrudRepository<PhotoUrl, Long> {

}