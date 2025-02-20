package com.umc7th.a1grade.domain.question.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.question.converter.QuestionStorageConverter;
import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO.UserQuestionIdListDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO.UserQuestionListDTO;
import com.umc7th.a1grade.domain.question.entity.QuestionType;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
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
  public UserQuestionListDTO getStorageQuestionsByQuestionType(
      @AuthenticationPrincipal UserDetails userDetails,
      QuestionType questionType,
      Long cursor,
      boolean isOverLimit) {

    User user = utils.getUserByUsername(userDetails.getUsername());

    List<UserQuestion> userQuestions;

    Pageable pageable = PageRequest.of(0, 12, Sort.by("id").descending());

    try {
      userQuestions =
          (isOverLimit)
              ? userQuestionRepository.findByUserIdAndOld(user.getId(), cursor, pageable)
              : userQuestionRepository.findByUserIdAndQuestionType(
                  user.getId(), questionType, cursor, pageable);
    } catch (DataAccessException e) {
      throw new GeneralException(QuestionStorageErrorStatus.QUESTION_DATABASE_ERROR);
    }

    if (questionType != QuestionType.AI
        && questionType != QuestionType.USER
        && questionType != null) {
      throw new GeneralException(QuestionStorageErrorStatus.INVALID_QUESTION_TYPE);
    }

    return QuestionStorageConverter.toQuestionListDTO(userQuestions);
  }

  @Override
  // @CacheEvict(value = "recentQuestions", key = "#userDetails.username")
  public boolean deleteStorageQuestions(
      @AuthenticationPrincipal UserDetails userDetails,
      UserQuestionIdListDTO userQuestionIdListDTO) {

    if (userQuestionIdListDTO == null || userQuestionIdListDTO.getUserQuestionIds().isEmpty()) {
      throw new GeneralException(QuestionStorageErrorStatus.EMPTY_LIST_REQUEST);
    }

    User user = utils.getUserByUsername(userDetails.getUsername());

    List<UserQuestion> userQuestionList =
        userQuestionRepository.findAllById(userQuestionIdListDTO.getUserQuestionIds());

    if (!(userQuestionIdListDTO.getUserQuestionIds().size() == userQuestionList.size())) {
      throw new GeneralException(QuestionStorageErrorStatus.RESOURCE_NOT_FOUND);
    }

    boolean isSuccess =
        userQuestionList.stream().allMatch(userQuestion -> userQuestion.getUser().equals(user));

    if (!isSuccess) {
      throw new GeneralException(QuestionStorageErrorStatus.FORBIDDEN_DELETE_REQUEST);
    }

    userQuestionRepository.deleteAll(userQuestionList);
    return true;
  }
}
