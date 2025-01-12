package com.umc7th.a1grade.domain.question.entity.mapping;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class QuestionLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userQuestionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "questionId", nullable = false)
	private Question question;

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
