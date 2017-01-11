package com.example.domain;

import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

/**
 * Created by pengg on 5/31/2016.
 */
@Entity
public class Pet implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private boolean status;

  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  private Category category;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pet", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private Set<PhotoUrl> photoUrls;

  @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
  @JoinTable(
      name="pet_tag",
      joinColumns=@JoinColumn(name="pet_id", referencedColumnName="id"),
      inverseJoinColumns=@JoinColumn(name="tag_id", referencedColumnName="id"))
  private Set<Tag> tags;

  public Pet() { }
  public Pet(long id) {
    this.id = id;
  }
  public Pet(String name,boolean status, Category category) {
    this.name = name;
    this.status = status;
    this.category = category;
  }

  public Category getCategory() {
    return category;
  }
  public void setCategory (Category c) {
    this.category = c;
  }

  public String getName() {
    return name;
  }
  public Long getId() {
    return id;
  }
  public void setId() {
    this.id = id;
  }
  public void setName (String name) {
    this.name = name;
  }

  public boolean getStatus() {
    return status;
  }
  public void setStatus (boolean status) {
    this.status = status;
  }

  public Set<Tag> getTags() {
	return tags;
  }

  public void setTags(Set<Tag> tags) {
	this.tags = tags;
  }

  public Set<PhotoUrl> getPhotoUrls() {return this.photoUrls;}
  public void setPhotoUrls(Set<PhotoUrl> photoUrls) {
    this.photoUrls = photoUrls;
  }
}