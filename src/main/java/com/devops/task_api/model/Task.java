package com.devops.task_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String status = "TODO";

    private LocalDateTime createdAt= LocalDateTime.now();

    public Task(){

    }

    public Long getId() {return id;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public String getStatus() {return status;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setId(Long id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setStatus(String status) {this.status = status;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}



