package com.umc7th.a1grade.domain.openAI.infrastructure;

import java.io.IOException;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.generateQuestionResponse;
import com.umc7th.a1grade.domain.openAI.exception.AIErrorStatus;
import com.umc7th.a1grade.global.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class GenerateQuestionManager {
  private final ChatClient chatClient;

  public generateQuestionResponse generateQuestion(String imageUrl) {

    BeanOutputConverter<generateQuestionResponse> parser =
        new BeanOutputConverter<>(generateQuestionResponse.class);

    String response =
        chatClient
            .prompt()
            .user(
                userSpec -> {
                  try {
                    userSpec
                        .text(
                            """
                    <role>
                    You are now an expert in mathematics curriculum subjects,
                    specializing in polynomial equations, inequalities, permutations and combinations,
                    matrices, equations of geometric figures, sets and propositions, functions and graphs,
                    exponential and logarithmic functions, trigonometric functions, sequences,
                    limits and continuity of functions, differentiation, integration,
                    probability and statistics, methods of differentiation, methods of integration,
                    mathematical limits, quadratic curves, three-dimensional geometry and coordinates, and vectors.
                    </role>
                    <purpose>
                    Purpose: The objective is to generate and provide similar problems
                    if the given image contains mathematical problems.
                    If the image does not contain mathematical problems, only an error message should be provided.
                    </purpose>
                    <instruction>
                    1. You must create a similar problem by either modifying the numerical values
                    or the problem itself from the provided image, such that the answer can be derived.
                    2. The scope of the generated problem must not go beyond the following subjects:
                    polynomials, equations and inequalities, permutations and combinations, matrices,
                    equations of geometric figures, sets and propositions, functions and graphs,
                    exponential and logarithmic functions, trigonometric functions, sequences,
                    limits and continuity of functions, differentiation, integration, probability and statistics,
                    methods of differentiation, methods of integration, mathematical limits, quadratic curves,
                    three-dimensional geometry and coordinates, and vectors.
                    3. The output must include the following three pieces of information: Similar problem (question)
                    , Answer to the similar problem (answer) Solution to the similar problem (memo),
                    presented in a step-by-step manner from Step 1 to Step 5.
                    4. When providing the answer to the similar problem,
                    it must be strictly the correct value in numerical or exact form,
                    e.g., if the answer is 25, it should only state "25" without any additional text.
                    5. Since you cannot provide images, you must create a similar problem that
                    can be solved without the need for an image if the provided image contains a diagram.
                    6. The solution (memo) must clearly outline the method for solving the similar problem
                    in a structured format (Step 1 to Step 5).
                    7. If the provided image is not related to mathematics,
                    the output (message) should be: “The format of the problem is incorrect.”
                    If all the required information for the similar problem is successfully generated,
                    the output (message) should be: “Successfully generated a similar problem.”
                    8. You should not present the same problem as the image. You must modify it to create a similar problem.
                    </instruction>
                    <example>
                    I will provide an example response. Even if an image with a problem similar
                    to the example response is input, you must not use the example values exactly as they are.
                    {
                      "question": {
                        "message": "Successfully generated a similar problem.",
                        "question": "Student A and Student B each have 10 apples.
                        If each student eats 1 apple, what is the average number of apples per student?",
                        "memo": "Step 1: This problem involves subtracting the number of apples eaten
                        from the total number of apples each student has. Step 2: Student A had 10 apples and ate 1,
                        so now they have 9 apples. Step 3: Student B had 10 apples and ate 1, so now they also have 9 apples.
                        Step 4: Add the number of apples Student A and Student B have. Step 5: To find the average,
                        divide the total number of apples by the total number of students, which is 2.",
                        "answer": "9"
                      }
                    }
                    </example>
                    The format is as follows. 모든 값은 한국어로 저장되어야 합니다. 한국어로 번역하여 보내주세요.
                    """
                                + parser.getFormat())
                        .media(MimeTypeUtils.IMAGE_JPEG, new UrlResource(imageUrl));
                  } catch (IOException e) {
                    throw new GeneralException(AIErrorStatus._FILE_SERVER_ERROR);
                  }
                })
            .call()
            .content();
    return parser.convert(response);
  }
}
