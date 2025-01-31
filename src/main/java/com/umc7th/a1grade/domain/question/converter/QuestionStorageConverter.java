package com.umc7th.a1grade.domain.question.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO.QuestionDTO;
import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionStorageConverter {

  public static QuestionStorageResponseDTO.QuestionDTO toQuestionDTO(Question question) {
    return QuestionStorageResponseDTO.QuestionDTO.builder()
        .id(question.getId())
        .memo(question.getMemo())
        .imageUrl(question.getImageUrl())
        .type(question.getType())
        .answer(question.getAnswer())
        .build();
  }

  public static List<QuestionStorageResponseDTO.QuestionDTO> toQuestionsForList(
      List<UserQuestion> UserQuestions) {
    return UserQuestions.stream()
        .map(
            question ->
                QuestionStorageResponseDTO.QuestionDTO.builder()
                    .id(question.getId())
                    .memo(question.getQuestion().getMemo())
                    .imageUrl(question.getQuestion().getImageUrl())
                    .type(question.getQuestion().getType())
                    .answer(question.getQuestion().getAnswer())
                    .build())
        .collect(Collectors.toList());
  }

  public static QuestionStorageResponseDTO.QuestionListDTO toQuestionListDTO(
      List<QuestionDTO> qestionDTOList) {
    return QuestionStorageResponseDTO.QuestionListDTO.builder().questions(qestionDTOList).build();
  }
}
