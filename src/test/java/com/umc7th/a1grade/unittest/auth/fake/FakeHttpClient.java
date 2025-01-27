package com.umc7th.a1grade.unittest.auth.fake;

public interface FakeHttpClient {
  String post(String url);

  String get(String url);
}
