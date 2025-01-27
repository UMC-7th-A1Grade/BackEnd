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
public class QuestionReviewHistory extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_question_id", nullable = false)
  private UserQuestion userQuestion;

  @Column(name = "last_reviewed_at")
  // 특정 문제의 복습(?) 시점, 다시 푼 시점 체크를 위한 칼럼
  private LocalDateTime lastReviewedAt;

  @Column(name = "review_count")
  // 문제 풀이 횟수를 기록하여 1일, 1주 .. 단위로 안나오게 하기 위한 칼럼
  @Builder.Default
  private int reviewCount = 0;

  @Column(name = "is_forgotten")
  // 2달 뒤에 조회할 때 안나오게 하기 위해 망각? 여부 칼럼
  @Builder.Default
  private Boolean isForgotten = false;
}
