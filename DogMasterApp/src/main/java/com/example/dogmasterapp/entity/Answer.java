package com.example.dogmasterapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    public Question question;
    public String answer;
    public Integer votes;
    @Column(name = "is_best_one")
    public Boolean isBestOne;
    @ManyToOne
    @JoinColumn(name = "answerer_id")
    @JsonIgnore
    public User answerer;
    @CreatedDate
    public LocalDateTime answeredAt;
    @LastModifiedDate
    public LocalDateTime updatedAt;
}
