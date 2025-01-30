package com.umc7th.a1grade.domain.user.repository;

import com.umc7th.a1grade.domain.user.entity.mapping.UserCharacter;

public interface UserCharacterRepository {

  UserCharacter save(UserCharacter userCharacter);
}
