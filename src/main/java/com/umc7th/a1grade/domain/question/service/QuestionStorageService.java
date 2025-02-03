package com.umc7th.a1grade.domain.question.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO.UserQuestionIdListDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO.UserQuestionListDTO;
import com.umc7th.a1grade.domain.question.entity.QuestionType;

public interface QuestionStorageService {

  UserQuestionListDTO getStorageQuestions(UserDetails userDetails);

  UserQuestionListDTO getStorageQuestionsByQuestionType(
      UserDetails userDetails, QuestionType questionType);

  boolean deleteStorageQuestions(
      UserDetails userDetails, UserQuestionIdListDTO userQuestionIdListDTO);
}
