package com.umc7th.a1grade.domain.question.dto;

import java.util.List;

import com.umc7th.a1grade.domain.question.entity.Question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RandomQuestionDTO{
		List<Question> questions;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SubmitAnswerDTO{
		String answer;
		boolean isCorrect;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class getAnswerDTO{
		String answer;
	}


}
