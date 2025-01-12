package com.umc7th.a1grade.domain.question.service;

public interface UserQuestionService {
  void getAllUserQuestions();

  void getUserQuestion();

  void registerUserQuestionMemo();

  void patchUserQuestionMemo();

  void saveUserQuestion();

  void handleCreditConfirm(boolean confirm);

  void saveSimilarQuestion();
}
