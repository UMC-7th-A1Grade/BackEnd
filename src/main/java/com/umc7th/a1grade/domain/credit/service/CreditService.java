package com.umc7th.a1grade.domain.credit.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.umc7th.a1grade.domain.credit.dto.CreditResponse;

public interface CreditService {

  CreditResponse.createResponse createCredit(UserDetails userDetails);

  CreditResponse.getResponse getCredit(UserDetails userDetails);

  CreditResponse.getResponse updateCredit(UserDetails userDetails);
}
