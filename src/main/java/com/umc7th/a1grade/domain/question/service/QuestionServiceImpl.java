package com.umc7th.a1grade.domain.question.service;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.question.Converter.QuestionConverter;
import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;
import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
import com.umc7th.a1grade.domain.question.exception.status.QuestionErrorStatus;
import com.umc7th.a1grade.domain.question.repository.QuestionLogRepository;
import com.umc7th.a1grade.domain.question.repository.QuestionRepository;
import com.umc7th.a1grade.domain.question.repository.UserQuestionRepository;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.repository.UserRepository;
import com.umc7th.a1grade.global.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionLogRepository questionLogRepository;
  private final UserRepository userRepository;
  private final QuestionConverter questionConverter;
  private final UserQuestionRepository userQuestionRepository;

  @Override
  @Transactional(readOnly = true)
  public QuestionResponseDTO.RandomQuestionDTO getRecentQuestions(
      @AuthenticationPrincipal UserDetails userDetails) {

    User user = getUserByUsername(userDetails.getUsername());

    List<Question> RecentQuestions = questionRepository.findRecentQuestion(user.getId());

    if (RecentQuestions.isEmpty()) {
      throw new GeneralException(QuestionErrorStatus.QUESTION_NOT_FOUND);
    }

    List<QuestionResponseDTO.QuestionDTO> questionDTOList =
        questionConverter.toQuestionDTO(RecentQuestions);

    return questionConverter.randomQuestionDTO(questionDTOList);
  }

  @Override
  public QuestionResponseDTO.SubmitAnswerDTO submitAnswer(
      int questionNum, QuestionRequestDTO.submitAnswerDTO answer, UserDetails userDetails) {

    Question question = getQuestionById(questionNum);

    User user = getUserByUsername(userDetails.getUsername());

    UserQuestion userQuestion =
        userQuestionRepository
            .findByUserIdAndQuestionId(user.getId(), question.getId())
            .orElseThrow(() -> new GeneralException(QuestionErrorStatus.USER_QUESTION_NOT_FOUND));

    boolean isCorrect = question.getAnswer().equals(answer.getAnswer());

    questionLogRepository.save(
        questionConverter.toQuestionLog(user, userQuestion, answer, isCorrect));

    return questionConverter.toSubmitAnswerDTO(isCorrect);
  }

  @Override
  @Transactional(readOnly = true)
  public QuestionResponseDTO.GetAnswerDTO getAnswer(int questionNum) {
    Question question = getQuestionById(questionNum);
    return questionConverter.toGetAnswerDTO(question);
  }

  @Override
  @Transactional(readOnly = true)
  public QuestionResponseDTO.RandomFalseQuestionDTO getRandomFalseQuestions(
      UserDetails userDetails) {
    User user = getUserByUsername(userDetails.getUsername());

    List<Question> randomFalseQuestions =
        questionRepository.findQuestionsByUserAndType(user.getId());

    if (randomFalseQuestions.size() < 3) {
      throw new GeneralException(QuestionErrorStatus.INSUFFICENT_QUESTIONS);
    }

    List<QuestionResponseDTO.FalseQuestionDTO> falseQuestionDTO =
        questionConverter.toFalseQuestionDTO(randomFalseQuestions);

    return questionConverter.toRandomFalseQuestionDTO(falseQuestionDTO);
  }

  private Question getQuestionById(int questionNum) {
    if (questionNum <= 0) {
      throw new GeneralException(QuestionErrorStatus.INVALID_QUESTION_ID);
    }
    return questionRepository
        .findByNum(questionNum)
        .orElseThrow(() -> new GeneralException(QuestionErrorStatus.QUESTION_NOT_FOUND));
  }

  private User getUserByUsername(String username) {
    if (username == null || username.isEmpty()) {
      throw new GeneralException(UserErrorStatus._USER_INVALID);
    }
    return userRepository
        .findBySocailId(username)
        .orElseThrow(() -> new GeneralException(UserErrorStatus._USER_NOT_FOUND));
  }
}
