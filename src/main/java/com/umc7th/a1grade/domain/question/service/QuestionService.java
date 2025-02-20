package com.umc7th.a1grade.domain.question.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;

public interface QuestionService {
  QuestionResponseDTO.RandomQuestionDTO getRecentQuestions(UserDetails userDetails);

  QuestionResponseDTO.SubmitAnswerDTO submitAnswer(
      Long id, QuestionRequestDTO.submitAnswerDTO answer, UserDetails userDetails);

  QuestionResponseDTO.GetAnswerDTO getAnswer(Long id);

  QuestionResponseDTO.RandomFalseQuestionDTO getRandomFalseQuestions(UserDetails userDetails);

  QuestionResponseDTO.RandomFalseQuestionDTO testGetRandomFalseQuestions(UserDetails userDetails);

  QuestionResponseDTO.GetQuestionDTO getQuestion(Long id, UserDetails userDetails);

  QuestionResponseDTO.SaveUserQuestionDTO saveQuestion(
      QuestionRequestDTO.RequestSaveQuestionDTO requestSaveQuestionDTO, UserDetails userDetails);

  void isDuplicateQuestion(UserDetails userDetails, String imageUrl);
}
