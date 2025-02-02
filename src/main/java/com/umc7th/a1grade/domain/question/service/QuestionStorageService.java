package com.umc7th.a1grade.domain.question.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO;
import com.umc7th.a1grade.domain.question.entity.QuestionType;

public interface QuestionStorageService {

  QuestionStorageResponseDTO.QuestionListDTO getStorageQuestions(UserDetails userDetails);

  QuestionStorageResponseDTO.QuestionListDTO getStorageQuestionsByQuestionType(
      UserDetails userDetails, QuestionType questionType);
}
