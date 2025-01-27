package com.umc7th.a1grade.global.common;

import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.repository.UserRepository;
import com.umc7th.a1grade.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Utils {
    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new GeneralException(UserErrorStatus._USER_INVALID);
        }
        return userRepository
            .findBySocailId(username)
            .orElseThrow(() -> new GeneralException(UserErrorStatus._USER_NOT_FOUND));
    }
}
