package com.umc7th.a1grade.domain.question.entity.mapping;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class QuestionLog extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_question_Id", nullable = false)
  private UserQuestion userQuestion;

  @Column(nullable = false)
  @Comment("제출시간")
  private LocalDateTime submissionTime;

  @Column(nullable = true)
  @Comment("메모")
  private String memo;

  @Column(nullable = false)
  @Comment("정답 여부")
  private boolean isCorrect;
}