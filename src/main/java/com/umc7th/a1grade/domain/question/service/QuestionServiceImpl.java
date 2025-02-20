package com.umc7th.a1grade.domain.question.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.question.converter.QuestionConverter;
import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;
import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.question.entity.QuestionType;
import com.umc7th.a1grade.domain.question.entity.mapping.QuestionLog;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
import com.umc7th.a1grade.domain.question.exception.status.QuestionErrorStatus;
import com.umc7th.a1grade.domain.question.repository.QuestionLogRepository;
import com.umc7th.a1grade.domain.question.repository.QuestionRepository;
import com.umc7th.a1grade.domain.question.repository.UserQuestionRepository;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.global.common.Utils;
import com.umc7th.a1grade.global.exception.GeneralException;
import com.umc7th.a1grade.global.s3.entity.PathName;
import com.umc7th.a1grade.global.s3.service.S3Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionLogRepository questionLogRepository;
  private final QuestionConverter questionConverter;
  private final UserQuestionRepository userQuestionRepository;
  private final Utils utils;
  private final S3Service s3Service;

  @Override
  @Cacheable(value = "recentQuestions", key = "#userDetails.username")
  @Transactional(readOnly = true)
  public QuestionResponseDTO.RandomQuestionDTO getRecentQuestions(
      @AuthenticationPrincipal UserDetails userDetails) {

    User user = utils.getUserByUsername(userDetails.getUsername());

    Pageable pageable = PageRequest.of(0, 5);

    List<QuestionResponseDTO.QuestionDTO> recentQuestions =
        questionConverter.toQuestionDTO(
            userQuestionRepository.findRecentQuestions(user.getId(), pageable));

    return questionConverter.randomQuestionDTO(recentQuestions);
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

    String fileUrl = s3Service.base64UploadFile(PathName.NOTE, answer.getNote());

    questionLogRepository.save(
        questionConverter.toQuestionLog(user, userQuestion, fileUrl, isCorrect));

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
  public QuestionResponseDTO.GetQuestionDTO getQuestion(
      Long userQuestionId, UserDetails userDetails) {
    UserQuestion userQuestion =
        userQuestionRepository
            .findUserQuestion(userQuestionId)
            .orElseThrow(() -> new GeneralException(QuestionErrorStatus.USER_QUESTION_NOT_FOUND));

    Question question = userQuestion.getQuestion();

    List<String> memos =
        userQuestion.getQuestionLogs().stream()
            .map(QuestionLog::getNote)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    return questionConverter.toGetQuestionDTO(question, memos);
  }

  private Question getQuestionById(Long id) {
    if (id <= 0) {
      throw new GeneralException(QuestionErrorStatus.INVALID_QUESTION_ID);
    }
    return questionRepository
        .findById(id)
        .orElseThrow(() -> new GeneralException(QuestionErrorStatus.QUESTION_NOT_FOUND));
  }

  @Override
  @CacheEvict(value = "recentQuestions", key = "#userDetails.username")
  public QuestionResponseDTO.SaveUserQuestionDTO saveQuestion(
      QuestionRequestDTO.RequestSaveQuestionDTO requestSaveQuestionDTO, UserDetails userDetails) {

    User user = utils.getUserByUsername(userDetails.getUsername());

    if (requestSaveQuestionDTO.getType() != QuestionType.AI
        && requestSaveQuestionDTO.getType() != QuestionType.USER) {
      throw new GeneralException(QuestionErrorStatus.INVALID_QUESTION_TYPE);
    }

    Question question = QuestionConverter.toQuestion(requestSaveQuestionDTO);

    try {
      questionRepository.save(question);
    } catch (DataAccessException e) {
      throw new GeneralException(QuestionErrorStatus.QUESTION_DATABASE_ERROR);
    }

    UserQuestion userQuestion =
        UserQuestion.builder()
            .user(user)
            .question(question)
            .questionLogs(new ArrayList<>())
            .build();

    try {
      userQuestionRepository.save(userQuestion);
    } catch (DataAccessException e) {
      throw new GeneralException(QuestionErrorStatus.QUESTION_DATABASE_ERROR);
    }

    return QuestionConverter.toUserQuestionDTO(userQuestion);
  }
}
