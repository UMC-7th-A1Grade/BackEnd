package com.umc7th.a1grade.domain.user.entity.mapping;

import jakarta.persistence.*;

import com.umc7th.a1grade.domain.character.entity.Character;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.global.common.BaseTimeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name="unique_user_character",columnNames = {"user_id", "character_id"}),
                @UniqueConstraint(name="unique_activate_character", columnNames = {"user_id", "isActive"})
        }
)
public class UserCharacter extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "character_id", nullable = false)
  private Character character;

  private boolean isActive;
}
