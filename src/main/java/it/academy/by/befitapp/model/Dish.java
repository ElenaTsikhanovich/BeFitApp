package it.academy.by.befitapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dishes")
public class Dish implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @OneToMany(fetch = FetchType.EAGER)
  private List<Ingredient> ingredients;

  @Column(name = "createTime")
  private LocalDateTime createTime;

  @Column(name = "updateTime")
  private LocalDateTime updateTime;

  @OneToOne(fetch = FetchType.EAGER)
  private User userWhoUpdate;

  public Dish(){

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  public User getUser() {
    return userWhoUpdate;
  }

  public void setUser(User user) {
    this.userWhoUpdate = userWhoUpdate;
  }
}
