package com.umc7th.a1grade.domain.question.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.question.converter.QuestionStorageConverter;
import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO;
import com.umc7th.a1grade.domain.question.entity.QuestionType;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
import com.umc7th.a1grade.domain.question.exception.status.QuestionErrorStatus;
import com.umc7th.a1grade.domain.question.exception.status.QuestionStorageErrorStatus;
import com.umc7th.a1grade.domain.question.repository.UserQuestionRepository;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.global.common.Utils;
import com.umc7th.a1grade.global.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionStorageServiceImpl implements QuestionStorageService {

  private final UserQuestionRepository userQuestionRepository;
  private final Utils utils;

  @Override
  public QuestionStorageResponseDTO.QuestionListDTO getStorageQuestions(
      @AuthenticationPrincipal UserDetails userDetails) {

    User user = utils.getUserByUsername(userDetails.getUsername());

    List<UserQuestion> userQuestions;
    try {
      userQuestions = userQuestionRepository.findByUserId(user.getId());
    } catch (DataAccessException e) {
      throw new GeneralException(QuestionErrorStatus.QUESTION_DATABASE_ERROR);
    }

    return QuestionStorageConverter.toQuestionListDTO(userQuestions);
  }

  @Override
  public QuestionStorageResponseDTO.QuestionListDTO getStorageQuestionsByQuestionType(
      @AuthenticationPrincipal UserDetails userDetails, QuestionType questionType) {

    User user = utils.getUserByUsername(userDetails.getUsername());

    List<UserQuestion> userQuestions;
    try {
      userQuestions =
          userQuestionRepository.findByUserIdAndQuestionType(user.getId(), questionType);
    } catch (DataAccessException e) {
      throw new GeneralException(QuestionStorageErrorStatus.QUESTION_DATABASE_ERROR);
    }

    if (questionType != QuestionType.AI && questionType != QuestionType.USER) {
      throw new GeneralException(QuestionStorageErrorStatus.INVALID_QUESTION_TYPE);
    }

    return QuestionStorageConverter.toQuestionListDTO(userQuestions);
  }
}
