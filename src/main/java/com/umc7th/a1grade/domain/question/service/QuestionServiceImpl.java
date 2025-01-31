package com.umc7th.a1grade.domain.question.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.question.converter.QuestionConverter;
import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;
import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
import com.umc7th.a1grade.domain.question.exception.status.QuestionErrorStatus;
import com.umc7th.a1grade.domain.question.repository.QuestionLogRepository;
import com.umc7th.a1grade.domain.question.repository.QuestionRepository;
import com.umc7th.a1grade.domain.question.repository.UserQuestionRepository;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.global.common.Utils;
import com.umc7th.a1grade.global.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionLogRepository questionLogRepository;
  private final QuestionConverter questionConverter;
  private final UserQuestionRepository userQuestionRepository;
  private final Utils utils;

  @Override
  @Transactional(readOnly = true)
  public QuestionResponseDTO.RandomQuestionDTO getRecentQuestions(
      @AuthenticationPrincipal UserDetails userDetails) {

    User user = utils.getUserByUsername(userDetails.getUsername());

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
      Long id, QuestionRequestDTO.submitAnswerDTO answer, UserDetails userDetails) {
    Question question = getQuestionById(id);

    User user = utils.getUserByUsername(userDetails.getUsername());

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
  public QuestionResponseDTO.GetAnswerDTO getAnswer(Long id) {
    Question question = getQuestionById(id);
    return questionConverter.toGetAnswerDTO(question);
  }

  @Override
  @Transactional(readOnly = true)
  public QuestionResponseDTO.RandomFalseQuestionDTO getRandomFalseQuestions(
      UserDetails userDetails) {
    User user = utils.getUserByUsername(userDetails.getUsername());

    List<Question> randomFalseQuestions;
    try {
      randomFalseQuestions = questionRepository.findQuestionsByUserAndType(user.getId());
    } catch (DataAccessException e) {
      throw new GeneralException(QuestionErrorStatus.QUESTION_DATABASE_ERROR);
    }

    if (randomFalseQuestions == null || randomFalseQuestions.isEmpty()) {
      throw new GeneralException(QuestionErrorStatus.NO_QUESTIONS_FOUND);
    }

    if (randomFalseQuestions.size() < 3) {
      throw new GeneralException(QuestionErrorStatus.INSUFFICENT_QUESTIONS);
    }

    List<QuestionResponseDTO.FalseQuestionDTO> falseQuestionDTO =
        questionConverter.toFalseQuestionDTO(randomFalseQuestions);

    return questionConverter.toRandomFalseQuestionDTO(falseQuestionDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public QuestionResponseDTO.GetQuestionDTO getQuestion(Long id, UserDetails userDetails) {
    Question question = getQuestionById(id);
    User user = utils.getUserByUsername(userDetails.getUsername());
    return questionConverter.toGetQuestionDTO(question, user);
  }

  private Question getQuestionById(Long id) {
    if (id <= 0) {
      throw new GeneralException(QuestionErrorStatus.INVALID_QUESTION_ID);
    }
    return questionRepository
        .findById(id)
        .orElseThrow(() -> new GeneralException(QuestionErrorStatus.QUESTION_NOT_FOUND));
  }
}
