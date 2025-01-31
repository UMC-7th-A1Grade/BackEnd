package com.umc7th.a1grade.domain.question.converter;

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
import com.umc7th.a1grade.domain.question.exception.status.QuestionErrorStatus;
import com.umc7th.a1grade.domain.question.repository.QuestionLogRepository;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.global.exception.GeneralException;

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
                    .id(question.getId())
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
                    .id(question.getId())
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

  public QuestionResponseDTO.GetQuestionDTO toGetQuestionDTO(Question question, User user) {
    List<QuestionLog> questionLog =
        questionLogRepository.findByUserIdAndQuestionId(user.getId(), question.getId());
    if (questionLog.isEmpty()) {
      throw new GeneralException(QuestionErrorStatus.QUESTIONLOG_NOT_FOUND);
    }

    List<String> memos = questionLog.stream().map(QuestionLog::getNote).toList();
    return QuestionResponseDTO.GetQuestionDTO.builder()
        .answer(question.getAnswer())
        .memo(question.getMemo())
        .note(memos)
        .questionImg(question.getImageUrl())
        .build();
  }
}
