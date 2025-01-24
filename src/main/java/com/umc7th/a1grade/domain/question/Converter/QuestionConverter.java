package com.umc7th.a1grade.domain.question.Converter;

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

  public QuestionLog toQuestionLog(
      User user,
      UserQuestion userQuestion,
      QuestionRequestDTO.submitAnswerDTO answer,
      boolean isCorrect) {
    return QuestionLog.builder()
        .user(user)
        .userQuestion(userQuestion)
        .submissionTime(LocalDateTime.now())
        .memo(answer.getMemo())
        .isCorrect(isCorrect)
        .build();
  }

  public QuestionResponseDTO.SubmitAnswerDTO toSubmitAnswerDTO(boolean isCorrect) {
    return QuestionResponseDTO.SubmitAnswerDTO.builder().isCorrect(isCorrect).build();
  }

  public QuestionResponseDTO.GetAnswerDTO toGetAnswerDTO(Question question) {
    return QuestionResponseDTO.GetAnswerDTO.builder()
        .answer(question.getAnswer())
        .content(question.getContent())
        .build();
  }

  public List<QuestionResponseDTO.QuestionDTO> toQuestionDTO(
      List<Question> questions, Long userId) {
    return questions.stream()
        .map(
            question -> {
              String memoImg =
                  questionLogRepository
                      .findLatestMemoByUserAndQuestion(userId, question.getId())
                      .orElse(null);

              return QuestionResponseDTO.QuestionDTO.builder()
                  .num(question.getNum())
                  .questionImg(question.getImageUrl())
                  .answer(question.getAnswer())
                  .explanation(question.getContent())
                  .memoImg(memoImg)
                  .build();
            })
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
}
