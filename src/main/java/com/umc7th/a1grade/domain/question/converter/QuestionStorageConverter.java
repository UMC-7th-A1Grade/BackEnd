package com.umc7th.a1grade.domain.question.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionStorageConverter {

  public static QuestionStorageResponseDTO.QuestionListDTO toQuestionListDTO(
      List<UserQuestion> userQuestions) {
    return QuestionStorageResponseDTO.QuestionListDTO.builder()
        .questions(
            userQuestions.stream()
                .map(
                    userQuestion ->
                        QuestionStorageResponseDTO.UserQuestionDTO.builder()
                            .userQuestionId(userQuestion.getId())
                            .imageUrl(userQuestion.getQuestion().getImageUrl())
                            .type(userQuestion.getQuestion().getType())
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
