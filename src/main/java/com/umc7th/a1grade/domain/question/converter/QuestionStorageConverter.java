package com.umc7th.a1grade.domain.question.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO;
import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionStorageConverter {

  public static QuestionStorageResponseDTO.QuestionDTO toQuestionDTO(Question question) {
    return QuestionStorageResponseDTO.QuestionDTO.builder()
        .id(question.getId())
        .imageUrl(question.getImageUrl())
        .type(question.getType())
        .build();
  }

  public static QuestionStorageResponseDTO.QuestionListDTO toQuestionListDTO(
      List<UserQuestion> userQuestions) {
    return QuestionStorageResponseDTO.QuestionListDTO.builder()
        .questions(
            userQuestions.stream()
                .map(userQuestion -> toQuestionDTO(userQuestion.getQuestion()))
                .collect(Collectors.toList()))
        .build();
  }
}
