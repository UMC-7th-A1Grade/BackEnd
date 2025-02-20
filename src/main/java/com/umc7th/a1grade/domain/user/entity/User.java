package com.umc7th.a1grade.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;
import com.umc7th.a1grade.domain.user.entity.mapping.UserCharacter;
import com.umc7th.a1grade.global.common.BaseTimeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  private SocialType socialType;

  @NotNull private String socialId;

  @NotNull private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String nickName;

  @ColumnDefault("100")
  @Builder.Default
  private Integer credit = 100;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<UserCharacter> userCharacters = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserQuestion> userQuestions = new ArrayList<>();

  public void setRole(Role role) {
    this.role = role;
  }

  public void setNickName(String nickname) {
    this.nickName = nickname;
  }

  public void setCredit(Integer credit) {
    this.credit = credit;
  }
}
