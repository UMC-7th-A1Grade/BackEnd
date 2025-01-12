package com.umc7th.a1grade.domain.question.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
	@Override
	@Transactional(readOnly = true)
	public void getRandomQuestions() {
	}
}
