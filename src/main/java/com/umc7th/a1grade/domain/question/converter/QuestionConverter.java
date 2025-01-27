package com.umc7th.a1grade.domain.question.converter;

import com.umc7th.a1grade.domain.question.entity.mapping.QuestionReviewHistory;
import com.umc7th.a1grade.domain.question.repository.QuestionReviewHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO.QuestionDTO;
import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.question.entity.mapping.QuestionLog;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
import com.umc7th.a1grade.domain.question.repository.QuestionLogRepository;
import com.umc7th.a1grade.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionConverter {

  private final QuestionLogRepository questionLogRepository;
  private final QuestionReviewHistoryRepository questionReviewHistoryRepository;

  public QuestionLog toQuestionLog(
      User user,
      UserQuestion userQuestion,
      QuestionRequestDTO.submitAnswerDTO answer,
      boolean isCorrect) {
    return QuestionLog.builder()
        .user(user)
        .userQuestion(userQuestion)
        .submissionTime(LocalDateTime.now())
        .note(answer.getNote())
        .isCorrect(isCorrect)
        .build();
  }

  public QuestionResponseDTO.SubmitAnswerDTO toSubmitAnswerDTO(boolean isCorrect) {
    return QuestionResponseDTO.SubmitAnswerDTO.builder().isCorrect(isCorrect).build();
  }

  public QuestionResponseDTO.GetAnswerDTO toGetAnswerDTO(Question question) {
    return QuestionResponseDTO.GetAnswerDTO.builder()
        .answer(question.getAnswer())
        .memo(question.getMemo())
        .build();
  }

  public List<QuestionResponseDTO.QuestionDTO> toQuestionDTO(List<Question> questions) {
    return questions.stream()
        .map(
            question ->
                QuestionResponseDTO.QuestionDTO.builder()
                    .num(question.getNum())
                    .questionImg(question.getImageUrl())
                    .build())
        .collect(Collectors.toList());
  }

  public List<QuestionResponseDTO.FalseQuestionDTO> toFalseQuestionDTO(List<Question> questions) {

    List<Long> questionIds = questions.stream().map(Question::getId).collect(Collectors.toList());

    Set<Long> questionLogExistsIds = questionLogRepository.findQuestionIdsWithLogs(questionIds);

    return questions.stream()
        .map(
            question ->
                QuestionResponseDTO.FalseQuestionDTO.builder()
                    .isSubmitted(questionLogExistsIds.contains(question.getId()))
                    .questionImg(question.getImageUrl())
                    .num(question.getNum())
                    .build())
        .collect(Collectors.toList());
  }

  public QuestionResponseDTO.RandomQuestionDTO randomQuestionDTO(
      List<QuestionDTO> questionDTOList) {
    return QuestionResponseDTO.RandomQuestionDTO.builder().questions(questionDTOList).build();
  }

  public QuestionResponseDTO.RandomFalseQuestionDTO toRandomFalseQuestionDTO(
      List<QuestionResponseDTO.FalseQuestionDTO> falseQuestionDTOS) {
    return QuestionResponseDTO.RandomFalseQuestionDTO.builder()
        .questions(falseQuestionDTOS)
        .build();
  }

  public QuestionReviewHistory toQuestionReviewHistory(UserQuestion userQuestion) {

    QuestionReviewHistory history =
        questionReviewHistoryRepository
            .findByUserQuestionId(userQuestion.getId())
            .map(
                existingHistory -> {
                  existingHistory.setReviewCount(
                      existingHistory.getReviewCount() + 1);
                  existingHistory.setLastReviewedAt(LocalDateTime.now());
                  return existingHistory;
                })
            .orElseGet(
                () ->
                    QuestionReviewHistory.builder()
                        .userQuestion(userQuestion)
                        .reviewCount(1)
                        .lastReviewedAt(LocalDateTime.now())
                        .isForgotten(false)
                        .build());
    return history;
  }
}