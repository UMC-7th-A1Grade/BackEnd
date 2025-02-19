package com.umc7th.a1grade.domain.question.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO;
<<<<<<< HEAD
import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO.UserQuestionListDTO;
=======
>>>>>>> d2bea99 (♻️ Refactor: 저장소 문제 조회 리턴값 수정)
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionStorageConverter {

<<<<<<< HEAD
  public static UserQuestionListDTO toQuestionListDTO(List<UserQuestion> userQuestions) {
    return UserQuestionListDTO.builder()
=======
  public static QuestionStorageResponseDTO.QuestionListDTO toQuestionListDTO(
      List<UserQuestion> userQuestions) {
    return QuestionStorageResponseDTO.QuestionListDTO.builder()
>>>>>>> d2bea99 (♻️ Refactor: 저장소 문제 조회 리턴값 수정)
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
        .cursor(
            userQuestions.isEmpty() ? null : userQuestions.get(userQuestions.size() - 1).getId())
        .build();
  }
}
