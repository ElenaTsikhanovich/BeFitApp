package it.academy.by.befitapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.academy.by.befitapp.model.api.Gender;
import it.academy.by.befitapp.model.api.LifeStyle;
import it.academy.by.befitapp.model.api.WeightGoal;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "profiles")
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private User user;

    @Column(name = "height")
    private Double height;

    @Column(name = "weightActual")
    private Double weightActual;

    @Column(name = "weightTarget")
    private Double weightTarget;

    @Column(name = "dateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "lifeStyle")
    @Enumerated(EnumType.STRING)
    private LifeStyle lifeStyle;

    @Column(name = "weightGoal")
    @Enumerated(EnumType.STRING)
    private WeightGoal weightGoal;

    @Column(name = "createTime")
    private LocalDateTime createTime;

    @Version
    @Column(name = "updateTime")
    private LocalDateTime updateTime;

    public Profile(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LifeStyle getLifeStyle() {
        return lifeStyle;
    }

    public void setLifeStyle(LifeStyle lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    public WeightGoal getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(WeightGoal weightGoal) {
        this.weightGoal = weightGoal;
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

    public Double getWeightActual() {
        return weightActual;
    }

    public void setWeightActual(Double weightActual) {
        this.weightActual = weightActual;
    }

    public Double getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(Double weightTarget) {
        this.weightTarget = weightTarget;
    }
}
