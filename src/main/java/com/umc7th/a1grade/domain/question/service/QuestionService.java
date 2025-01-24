package com.umc7th.a1grade.domain.question.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;

public interface QuestionService {
  QuestionResponseDTO.RandomQuestionDTO getRandomQuestions(UserDetails userDetails);

  QuestionResponseDTO.SubmitAnswerDTO submitAnswer(
      int questionNum, QuestionRequestDTO.submitAnswerDTO answer, UserDetails userDetails);

  QuestionResponseDTO.GetAnswerDTO getAnswer(int questionNum);

  QuestionResponseDTO.RandomFalseQuestionDTO getRandomFalseQuestions(UserDetails userDetails);
}
