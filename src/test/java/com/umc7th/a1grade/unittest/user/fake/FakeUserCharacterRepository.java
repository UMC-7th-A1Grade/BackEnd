package com.umc7th.a1grade.unittest.user.fake;

import com.umc7th.a1grade.domain.user.entity.mapping.UserCharacter;
import com.umc7th.a1grade.domain.user.repository.UserCharacterRepository;

public class FakeUserCharacterRepository implements UserCharacterRepository {

  @Override
  public UserCharacter save(UserCharacter userCharacter) {
    return null;
  }
}
