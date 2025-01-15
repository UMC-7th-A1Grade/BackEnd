package com.umc7th.a1grade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class A1gradeApplication {

  public static void main(String[] args) {
    SpringApplication.run(A1gradeApplication.class, args);
  }
}
