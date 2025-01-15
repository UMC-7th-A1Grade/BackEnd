package com.umc7th.a1grade.domain.question.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.Comment;

import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
import com.umc7th.a1grade.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Question extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("문제번호")
  @Column(nullable = false)
  private Integer num;

  @Comment("풀이")
  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Comment("문제 이미지 URL")
  @Column(nullable = false)
  private String imageUrl;

  @Comment("종류")
  @Enumerated(EnumType.STRING)
  private QuestionType type;

  @Comment("정답")
  @Column(nullable = false)
  private String answer;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserQuestion> userQuestions = new ArrayList<>();
}
