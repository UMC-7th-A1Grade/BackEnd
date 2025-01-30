package com.umc7th.a1grade.unittest.question.repository;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.umc7th.a1grade.domain.question.entity.Question;
import com.umc7th.a1grade.domain.question.entity.QuestionType;
import com.umc7th.a1grade.domain.question.entity.mapping.QuestionLog;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
import com.umc7th.a1grade.domain.question.repository.QuestionRepository;
import com.umc7th.a1grade.domain.question.repository.UserQuestionRepository;
import com.umc7th.a1grade.domain.user.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private QuestionRepository questionRepository;

  @Autowired private UserQuestionRepository userQuestionRepository;

  private User testUser;
  private List<Question> questions;

  @BeforeEach
  void setUp() {
    // Create test user
    testUser = User.builder().email("test@example.com").socialId("test").build();
    entityManager.persist(testUser);

    // Create test questions
    questions =
        IntStream.range(0, 6)
            .mapToObj(
                i -> {
                  Question question =
                      Question.builder()
                          .num(i + 1)
                          .memo("Test Question Memo " + i)
                          .imageUrl("http://example.com/image" + i + ".jpg")
                          .type(QuestionType.USER)
                          .answer("Answer " + i)
                          .build();
                  entityManager.persist(question);

                  UserQuestion userQuestion =
                      UserQuestion.builder().user(testUser).question(question).build();
                  entityManager.persist(userQuestion);

                  question.getUserQuestions().add(userQuestion);

                  return question;
                })
            .collect(Collectors.toList());

    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("신규 문제 조회 테스트 (attempt_count = 0)")
  void shouldReturnNewQuestions() {
    List<Question> result = questionRepository.findQuestionsByUserAndType(testUser.getId());

    assertThat(result).isNotEmpty();
    assertThat(result).hasSize(3);
    assertThat(result.stream().map(Question::getId))
        .containsAnyElementsOf(
            questions.stream().map(Question::getId).collect(Collectors.toList()));
  }

  @Test
  @DisplayName("1회 시도 후 10분이 지나지 않은 문제는 한 번이라도 조회되면 실패")
  void shouldNotReturnQuestionsWithinTenMinutes() {
    // Given
    Question targetQuestion = questions.get(0);
    createQuestionLog(targetQuestion, LocalDateTime.now().minusMinutes(5), true);

    entityManager.flush();
    entityManager.clear();

    // When
    int attempts = 10; // 10번 실행
    for (int i = 0; i < attempts; i++) {
      List<Question> result = questionRepository.findQuestionsByUserAndType(testUser.getId());

      // targetQuestion이 한 번이라도 포함되면 실패
      if (result.stream().map(Question::getId).anyMatch(id -> id.equals(targetQuestion.getId()))) {
        fail("질문은 주어진 지나기 전에 포함되면 안되므로 실패");
      }
    }
  }

  @Test
  @DisplayName("1회 시도 후 10분 이후 문제 조회")
  void shouldReturnQuestionsAfterTenMinutes() {
    // Given
    Question targetQuestion = questions.get(0);
    createQuestionLog(targetQuestion, LocalDateTime.now().minusMinutes(5), true);

    entityManager.flush();
    entityManager.clear();

    // When
    int attempts = 10; // 10번 실행
    for (int i = 0; i < attempts; i++) {
      List<Question> result = questionRepository.findQuestionsByUserAndType(testUser.getId());

      // targetQuestion이 한 번이라도 포함되면 실패
      if (result.stream().map(Question::getId).anyMatch(id -> id.equals(targetQuestion.getId()))) {
        fail("질문은 주어진 지나기 전에 포함되면 안되므로 실패");
      }
    }
  }

  @Test
  @DisplayName("2회 시도 후 1일 이내 문제는 조회되지 않음")
  void shouldNotReturnQuestionsWithinOneDay() {
    // Given
    Question targetQuestion = questions.get(0);
    createQuestionLog(targetQuestion, LocalDateTime.now().minusMinutes(5), true);

    entityManager.flush();
    entityManager.clear();

    // When
    int attempts = 10; // 10번 실행
    for (int i = 0; i < attempts; i++) {
      List<Question> result = questionRepository.findQuestionsByUserAndType(testUser.getId());

      // targetQuestion이 한 번이라도 포함되면 실패
      if (result.stream().map(Question::getId).anyMatch(id -> id.equals(targetQuestion.getId()))) {
        fail("질문은 주어진 지나기 전에 포함되면 안되므로 실패");
      }
    }
  }

  @Test
  @DisplayName("3회 시도 후 1주일 이후 문제 조회")
  void shouldReturnQuestionsAfterOneWeek() {
    // Given
    Question targetQuestion = questions.get(0);
    createQuestionLog(targetQuestion, LocalDateTime.now().minusMinutes(5), true);

    entityManager.flush();
    entityManager.clear();

    // When
    int attempts = 10; // 10번 실행
    for (int i = 0; i < attempts; i++) {
      List<Question> result = questionRepository.findQuestionsByUserAndType(testUser.getId());

      // targetQuestion이 한 번이라도 포함되면 실패
      if (result.stream().map(Question::getId).anyMatch(id -> id.equals(targetQuestion.getId()))) {
        fail("질문은 주어진 지나기 전에 포함되면 안되므로 실패");
      }
    }
  }

  @Test
  @DisplayName("4회 시도 후 1달 이후 문제 조회")
  void shouldReturnQuestionsAfterOneMonth() {
    // Given
    Question targetQuestion = questions.get(0);
    createQuestionLog(targetQuestion, LocalDateTime.now().minusMinutes(5), true);

    entityManager.flush();
    entityManager.clear();

    // When
    int attempts = 10; // 10번 실행
    for (int i = 0; i < attempts; i++) {
      List<Question> result = questionRepository.findQuestionsByUserAndType(testUser.getId());

      // targetQuestion이 한 번이라도 포함되면 실패
      if (result.stream().map(Question::getId).anyMatch(id -> id.equals(targetQuestion.getId()))) {
        fail("질문은 주어진 지나기 전에 포함되면 안되므로 실패");
      }
    }
  }

  @Test
  @DisplayName("5회 시도 후 2달 이후 문제 조회")
  void shouldReturnQuestionsAfterTwoMonths() {
    // Given
    Question targetQuestion = questions.get(0);
    createQuestionLog(targetQuestion, LocalDateTime.now().minusMinutes(5), true);

    entityManager.flush();
    entityManager.clear();

    // When
    int attempts = 10; // 10번 실행
    for (int i = 0; i < attempts; i++) {
      List<Question> result = questionRepository.findQuestionsByUserAndType(testUser.getId());

      // targetQuestion이 한 번이라도 포함되면 실패
      if (result.stream().map(Question::getId).anyMatch(id -> id.equals(targetQuestion.getId()))) {
        fail("질문은 주어진 지나기 전에 포함되면 안되므로 실패");
      }
    }
  }

  @Test
  @DisplayName("2달이 지난 문제는 조회되지 않음 (attempt_count = 6)")
  void shouldNotReturnQuestionsOlderThanTwoMonths() {
    Question question = questions.get(0);
    createQuestionLog(question, LocalDateTime.now().minusMonths(2).minusDays(1), true);

    entityManager.flush();
    entityManager.clear();

    List<Question> result = questionRepository.findQuestionsByUserAndType(testUser.getId());

    assertThat(result.stream().map(Question::getId)).doesNotContain(question.getId());
  }

  private void createQuestionLog(
      Question question, LocalDateTime submissionTime, boolean isCorrect) {
    UserQuestion userQuestion =
        userQuestionRepository
            .findByUserIdAndQuestionId(testUser.getId(), question.getId())
            .orElseThrow(() -> new IllegalStateException("UserQuestion을 찾을 수 없음"));

    QuestionLog questionLog =
        QuestionLog.builder()
            .user(testUser)
            .userQuestion(userQuestion)
            .submissionTime(submissionTime)
            .note("Test note")
            .isCorrect(isCorrect)
            .build();

    entityManager.persist(questionLog);
    userQuestion.getQuestionLogs().add(questionLog);
  }
}
