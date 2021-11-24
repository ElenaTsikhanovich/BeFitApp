package it.academy.by.befitapp.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens")
public class ConformationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String conformationToken;

    @Column(name = "createTime")
    private LocalDateTime createTime;

    @OneToOne
    @JoinColumn(nullable = false,name = "user_id")
    private User user;

    public ConformationToken(User user) {
        this.user=user;
        createTime=LocalDateTime.now();
        conformationToken= UUID.randomUUID().toString();
    }

    public ConformationToken() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConformationToken() {
        return conformationToken;
    }

    public void setConformationToken(String conformationToken) {
        this.conformationToken = conformationToken;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
