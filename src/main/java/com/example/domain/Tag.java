package com.example.domain;

/**
 * Created by pengg on 5/31/2016.
 */
import org.hibernate.annotations.NaturalId;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Tag  implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private String name;

  @ManyToMany(mappedBy="tags")
  private Set<Pet> pets;

  protected Tag() {
  }
  public Long getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public void setId() {
    this.id = id;
  }
  public void setName (String name) {
    this.name = name;
  }
  public Tag(String name) {
    this.name = name;
  }
}
