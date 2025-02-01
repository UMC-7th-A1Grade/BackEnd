package com.umc7th.a1grade.domain.question.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;
import com.umc7th.a1grade.global.s3.PathName;

public interface QuestionService {
  QuestionResponseDTO.RandomQuestionDTO getRecentQuestions(UserDetails userDetails);

  QuestionResponseDTO.SubmitAnswerDTO submitAnswer(
      Long id, QuestionRequestDTO.submitAnswerDTO answer, UserDetails userDetails);

  QuestionResponseDTO.GetAnswerDTO getAnswer(Long id);

  QuestionResponseDTO.RandomFalseQuestionDTO getRandomFalseQuestions(UserDetails userDetails);

  QuestionResponseDTO.ImgUrlDTO ImgUpload(PathName pathName, MultipartFile multipartFile);
}
