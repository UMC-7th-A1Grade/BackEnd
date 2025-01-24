package com.umc7th.a1grade;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.question.repository.QuestionRepository;

@SpringBootTest
@ActiveProfiles("local")
public class QuestionRepositoryTest {

  @Autowired private QuestionRepository questionRepository;

  @Test
  void compareRandomQuestionSelectionMethods() {
    Long userId = 1L; // 테스트용 사용자 ID

    // 테스트 데이터 삽입
    insertTestData(userId);

    // 1. 메모리 셔플 선택
    long start1 = System.nanoTime();
    List<Question> memoryShuffleQuestions = getRandomQuestionsByMemoryShuffle(userId);
    long end1 = System.nanoTime();

    // 2. 페이징 랜덤 선택
    long start2 = System.nanoTime();
    List<Question> pageRandomQuestions = getRandomQuestionsByPage(userId);
    long end2 = System.nanoTime();

    // 3. LIMIT 사용 랜덤 선택
    long start3 = System.nanoTime();
    List<Question> limitRandomQuestions = getRandomQuestionsByLimit(userId);
    long end3 = System.nanoTime();

    // 성능 및 결과 검증
    System.out.println("메모리 셔플 소요시간: " + (end1 - start1) + "ns");
    System.out.println("페이징 랜덤 선택 소요시간: " + (end2 - start2) + "ns");
    System.out.println("LIMIT 랜덤 선택 소요시간: " + (end3 - start3) + "ns");

    // 결과 크기와 중복 검증
    assertThat(memoryShuffleQuestions).hasSize(3);
    assertThat(pageRandomQuestions).hasSize(3);
    assertThat(limitRandomQuestions).hasSize(3);

    assertThat(memoryShuffleQuestions).doesNotHaveDuplicates();
    assertThat(pageRandomQuestions).doesNotHaveDuplicates();
    assertThat(limitRandomQuestions).doesNotHaveDuplicates();
  }

  private void insertTestData(Long userId) {
    for (int i = 1; i <= 100; i++) {
      Question question =
          Question.builder()
              .num(i)
              .content("Question content " + i)
              .imageUrl("https://example.com/question" + i + ".png")
              .answer("Answer " + i)
              .build();
      questionRepository.save(question);
    }
  }

  private List<Question> getRandomQuestionsByMemoryShuffle(Long userId) {
    List<Question> questions = questionRepository.findQuestionsByUser(userId);
    Collections.shuffle(questions);
    return questions.stream().limit(3).collect(Collectors.toList());
  }

  private List<Question> getRandomQuestionsByPage(Long userId) {
    Pageable pageable = PageRequest.of(0, 3);
    return questionRepository.findRandomQuestionsByUserNative(userId, pageable).getContent();
  }

  private List<Question> getRandomQuestionsByLimit(Long userId) {
    return questionRepository.findQuestionsByUserNative(userId);
  }
  }

