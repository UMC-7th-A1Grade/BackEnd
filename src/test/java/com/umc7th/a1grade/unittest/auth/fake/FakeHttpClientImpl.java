package com.umc7th.a1grade.unittest.auth.fake;

import java.util.HashMap;
import java.util.Map;

public class FakeHttpClientImpl implements FakeHttpClient {
  private final Map<String, String> fakeResponses = new HashMap<>();

  public FakeHttpClientImpl() {
    fakeResponses.put(
        "http://localhost:8888/oauth2/token",
        "{ \"access_token\": \"mock-access-token\", \"expires_in\": 3600, \"token_type\": \"Bearer\" }");

    fakeResponses.put(
        "http://localhost:8888/oauth2/userinfo",
        "{ \"sub\": \"test-social-id\", \"email\": \"test@example.com\", \"name\": \"Fake User\", \"picture\": \"https://fake-image.com/profile.jpg\" }");
  }

  @Override
  public String post(String url) {
    if (!fakeResponses.containsKey(url)) {
      throw new IllegalArgumentException("Invalid URL for POST request: " + url);
    }
    return fakeResponses.get(url);
  }

  @Override
  public String get(String url) {
    if (!fakeResponses.containsKey(url)) {
      throw new IllegalArgumentException("Invalid URL for GET request: " + url);
    }
    return fakeResponses.get(url);
  }
}
