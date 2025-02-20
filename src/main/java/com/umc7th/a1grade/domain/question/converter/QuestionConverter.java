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
import com.umc7th.a1grade.domain.question.repository.QuestionLogRepository;
import com.umc7th.a1grade.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionConverter {

  private final QuestionLogRepository questionLogRepository;

  public QuestionLog toQuestionLog(
      User user, UserQuestion userQuestion, String fileUrl, boolean isCorrect) {
    return QuestionLog.builder()
        .user(user)
        .userQuestion(userQuestion)
        .submissionTime(LocalDateTime.now())
        .note(fileUrl)
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

  public static Question toQuestion(
      QuestionRequestDTO.RequestSaveQuestionDTO requestSaveQuestionDTO) {
    return Question.builder()
        .memo(requestSaveQuestionDTO.getMemo())
        .imageUrl(requestSaveQuestionDTO.getImageUrl())
        .type(requestSaveQuestionDTO.getType())
        .answer(requestSaveQuestionDTO.getAnswer())
        .build();
  }

  public static QuestionResponseDTO.SaveUserQuestionDTO toUserQuestionDTO(
      UserQuestion userQuestion) {
    return QuestionResponseDTO.SaveUserQuestionDTO.builder()
        .userQuestionId(userQuestion.getId())
        .build();
  }

  public QuestionResponseDTO.GetQuestionDTO toGetQuestionDTO(
      Question question, List<String> memos) {

    return QuestionResponseDTO.GetQuestionDTO.builder()
        .answer(question.getAnswer())
        .memo(question.getMemo())
        .note(memos)
        .questionImg(question.getImageUrl())
        .build();
  }

  public List<QuestionDTO> toQuestionDTO(List<UserQuestion> userQuestions) {
    return userQuestions.stream()
        .map(
            userQuestion ->
                QuestionDTO.builder()
                    .id(userQuestion.getId())
                    .questionImg(userQuestion.getQuestion().getImageUrl())
                    .build())
        .collect(Collectors.toList());
  }
}
