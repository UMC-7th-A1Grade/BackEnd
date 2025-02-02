package com.umc7th.a1grade.domain.openAI.infrastructure;

import java.io.IOException;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.confirmQuestionResponse;
import com.umc7th.a1grade.domain.openAI.exception.AIErrorStatus;
import com.umc7th.a1grade.global.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ConfirmQuestionManager {
  private final ChatClient chatClient;

  public confirmQuestionResponse confirmQuestion(MultipartFile image) {

    BeanOutputConverter<confirmQuestionResponse> parser =
        new BeanOutputConverter<>(confirmQuestionResponse.class);

    String response =
        chatClient
            .prompt()
            .user(
                userSpec -> {
                  try {
                    byte[] imageBytes = image.getBytes(); // MultipartFile에서 바이트 배열을 얻음
                    Resource imageResource =
                        new ByteArrayResource(imageBytes); // 바이트 배열로부터 Resource 생성
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
                    Purpose: The image is for the purpose of identifying a math problem.
                    </purpose>
                    <instruction>
                    1. The transmitted image must be verified to determine if it is a valid math problem image.
                    2. If the transmitted image is unreadable or not a math problem, it should output false.
                    3. The problem in the transmitted image must be modified to derive a new value.
                    4. If the transmitted image is a valid math problem, it should output true.
                    </instruction>
                    The format is as follows.
                    """
                                + parser.getFormat())
                        .media(MimeTypeUtils.IMAGE_JPEG, imageResource);
                  } catch (IOException e) {
                    throw new GeneralException(AIErrorStatus._FILE_SERVER_ERROR);
                  }
                })
            .call()
            .content();
    return parser.convert(response);
  }
}