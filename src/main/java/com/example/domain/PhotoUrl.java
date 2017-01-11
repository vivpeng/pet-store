package com.example.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by pengg on 5/31/2016.
 */

@Entity
public class PhotoUrl  implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private String url;

  @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
  private Pet pet;

  protected PhotoUrl() {
  }
  public Long getId() { return id; }
  public String getUrl() {
    return url;
  }
  public void setId() {
    this.id = id;
  }
  public void setUrl (String url) {
    this.url = url;
  }
  public PhotoUrl(String url, Pet pet) {
    this.url = url;
    this.pet = pet;
  }
}
