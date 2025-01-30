package com.umc7th.a1grade.domain.character.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
@Table(name = "characters")
public class Character extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String imageUrl;

  @Builder.Default
  @OneToMany(mappedBy = "character", cascade = CascadeType.ALL)
  private List<UserCharacter> userCharacters = new ArrayList<>();
}
