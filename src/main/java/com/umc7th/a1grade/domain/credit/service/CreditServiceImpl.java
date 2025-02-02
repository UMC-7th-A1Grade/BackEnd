package com.umc7th.a1grade.domain.credit.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.credit.dto.CreditResponse;
import com.umc7th.a1grade.domain.credit.dto.CreditResponse.createResponse;
import com.umc7th.a1grade.domain.credit.dto.CreditResponse.getResponse;
import com.umc7th.a1grade.domain.credit.entity.Credit;
import com.umc7th.a1grade.domain.credit.exception.CreditErrorStatus;
import com.umc7th.a1grade.domain.credit.repository.CreditRepository;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.repository.UserRepository;
import com.umc7th.a1grade.global.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

  private final CreditRepository creditRepository;
  private final UserRepository userRepository;

  @Override
  public CreditResponse.createResponse createCredit(UserDetails userDetails) {
    User user =
        userRepository
            .findBySocialId(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(CreditErrorStatus._USER_NOT_FOUND));

    if (creditRepository.findByUser_Email(user.getEmail()).isPresent()) {
      throw new GeneralException(CreditErrorStatus._CREDIT_EXIST);
    } else {
      Credit credit = Credit.builder().totalCredit(10).user(user).build();

      creditRepository.save(credit);

      return new createResponse(true);
    }
  }

  @Override
  public CreditResponse.getResponse getCredit(UserDetails userDetails) {
    User user =
        userRepository
            .findBySocialId(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(CreditErrorStatus._USER_NOT_FOUND));

    Credit credit =
        creditRepository
            .findByUser_Email(user.getEmail())
            .orElseThrow(() -> new GeneralException(CreditErrorStatus._CREDIT_NOT_FOUND));

    return new getResponse(true, credit.getTotalCredit());
  }

  @Override
  public CreditResponse.getResponse updateCredit(UserDetails userDetails) {
    User user =
        userRepository
            .findBySocialId(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(CreditErrorStatus._USER_NOT_FOUND));

    Credit credit =
        creditRepository
            .findByUser_Email(user.getEmail())
            .orElseThrow(() -> new GeneralException(CreditErrorStatus._CREDIT_NOT_FOUND));

    if (credit.getTotalCredit() > 0) {
      credit.setTotalCredit(credit.getTotalCredit() - 1);
      creditRepository.save(credit);
    } else {
      throw new GeneralException(CreditErrorStatus._CREDIT_COUNT_INVALID);
    }

    return new getResponse(true, credit.getTotalCredit());
  }
}
