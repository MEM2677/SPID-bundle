package it.entando.pa.spid.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

/*
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIzZEZCSmtCeFhmTUJOWDllZnowMWV6R1hNYms1X1pNR0pUTDhxMjJ6UXNnIn0.eyJleHAiOjE2NTE1NzAzNDMsImlhdCI6MTY1MTU3MDI4MywianRpIjoiMjdhN2UyY2EtZTM5ZC00ZjI3LWEwODctNTY0YzdlYjZlZmMyIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDkwL2F1dGgvcmVhbG1zL21hc3RlciIsInN1YiI6IjJjMDg4Y2FiLTlmMjItNDNiOC04NjUxLTVjMzdlOWIxNTg4ZSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsInNlc3Npb25fc3RhdGUiOiI2Yjk1YjA0Yy0yNjY5LTQ0MjctOWUxZi1kYjQ0NzVkZThkMTMiLCJhY3IiOiIxIiwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjZiOTViMDRjLTI2NjktNDQyNy05ZTFmLWRiNDQ3NWRlOGQxMyIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW4ifQ.utzkjHgX2ZblPYxvsS1R8wrTDvvTnnkNigKLenq-JF7NZNsHhFoO-vYGvxy6B31x_NRt2AbMWPZT1qS9s2kTGplxrkeAJwIn8vbDBq0CWYrelyePCv_wyEfQiyO3M5lXtblRc6Bm3tbzMZwSZPcTjPUCyi4xTaIeINfw04GT-t1rH_PtOvBPZ7vdgRovANnTFg1lySjKExDVmo2VWJjsmCN-h7Wns6Fvbd-fVR0j-npA-VYjzprt_hgpCm291QMSQiYN5U_ORsFGv0pXbbHeppmc9y0ew2kbXeWWuecyJXzKoQpL33yrEr5TBp4CIFsoe3APVkm3PkjzPjSdmIfMfg",
    "expires_in": 60,
    "refresh_expires_in": 1800,
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0YmU3NDAyNi1lODdhLTQyYTAtYmRhMy02YTAxOWEzN2YxNDgifQ.eyJleHAiOjE2NTE1NzIwODMsImlhdCI6MTY1MTU3MDI4MywianRpIjoiMTU0NTFiZTYtNDY2MS00YTVlLWE3MGEtY2IxOGM5NmFjZDIwIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDkwL2F1dGgvcmVhbG1zL21hc3RlciIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTA5MC9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJzdWIiOiIyYzA4OGNhYi05ZjIyLTQzYjgtODY1MS01YzM3ZTliMTU4OGUiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoiYWRtaW4tY2xpIiwic2Vzc2lvbl9zdGF0ZSI6IjZiOTViMDRjLTI2NjktNDQyNy05ZTFmLWRiNDQ3NWRlOGQxMyIsInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiI2Yjk1YjA0Yy0yNjY5LTQ0MjctOWUxZi1kYjQ0NzVkZThkMTMifQ.3d9VzjtdVs9ygrlCfRCcnAs-jwml5dFabrJET4UO9X0",
    "token_type": "Bearer",
    "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIzZEZCSmtCeFhmTUJOWDllZnowMWV6R1hNYms1X1pNR0pUTDhxMjJ6UXNnIn0.eyJleHAiOjE2NTE1NzAzNDMsImlhdCI6MTY1MTU3MDI4MywiYXV0aF90aW1lIjowLCJqdGkiOiJkNzlkN2I5Mi0wZmQ1LTQ3NzUtYmE5ZS1mYTI2ZjY3NzBiNmYiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwOTAvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWRtaW4tY2xpIiwic3ViIjoiMmMwODhjYWItOWYyMi00M2I4LTg2NTEtNWMzN2U5YjE1ODhlIiwidHlwIjoiSUQiLCJhenAiOiJhZG1pbi1jbGkiLCJzZXNzaW9uX3N0YXRlIjoiNmI5NWIwNGMtMjY2OS00NDI3LTllMWYtZGI0NDc1ZGU4ZDEzIiwiYXRfaGFzaCI6Ikd5THVSSlg0T1BISFgzdHllNWdDMUEiLCJhY3IiOiIxIiwic2lkIjoiNmI5NWIwNGMtMjY2OS00NDI3LTllMWYtZGI0NDc1ZGU4ZDEzIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiJ9.vLitswAYBXGV8d-kS8VMTaRMD9ZVDpN6AI5q2JlQdyIcrWlQg0dlGIcdpy942GRdqMToegJDQB6itIWQK3xhrAK3C-BpjaeCiE1gk5qroXO-VO6HjLlv0t2-ZnVdlefXwWM-zrnfU6PL3-hVFyx3GD8Hov9DzqmzTn_uhorsWMq6wBZxHVb-p1KbhX2pLhoDIcmWVvQ4FEGeNNjA4AZMMtCEUA2RhaNSDo_SmK99e7K7AWDmu3IOB-S9DDEuqwsYkRJEKsYqDvlseItXxAWWzoJgnNanWKmO_iXWIQ8kMmtOhLHe_LJJvM4OioHUqfjdP1P1xPf11LelTpADg2sBaA",
    "not-before-policy": 0,
    "session_state": "6b95b04c-2669-4427-9e1f-db4475de8d13",
    "scope": "openid email profile"
}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KCToken {

  private String accessToken;
  private Long expiresIn;
  private Long refreshExpiresIn;
  private String refreshToken;
  private String tokenType;
  private String idToken;
  private Long notBeforePolicy; // not-before-policy;
  private String sessionState;
  private String scope;

  public String getAccessToken() {
    return accessToken;
  }

  @JsonSetter("access_token")
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Long getExpiresIn() {
    return expiresIn;
  }

  @JsonSetter("expires_in")
  public void setExpiresIn(Long expiresIn) {
    this.expiresIn = expiresIn;
  }

  public Long getRefreshExpiresIn() {
    return refreshExpiresIn;
  }

  @JsonSetter("refresh_expires_in")
  public void setRefreshExpiresIn(Long refreshExpiresIn) {
    this.refreshExpiresIn = refreshExpiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  @JsonSetter("refresh_token")
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  @JsonSetter("token_type")
  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public String getIdToken() {
    return idToken;
  }

  @JsonSetter("id_token")
  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

  public Long getNotBeforePolicy() {
    return notBeforePolicy;
  }

  @JsonSetter("not-before-policy")
  public void setNotBeforePolicy(Long notBeforePolicy) {
    this.notBeforePolicy = notBeforePolicy;
  }

  public String getSessionState() {
    return sessionState;
  }

  @JsonSetter("session_state")
  public void setSessionState(String sessionState) {
    this.sessionState = sessionState;
  }

  public String getScope() {
    return scope;
  }

  @JsonSetter("scope")
  public void setScope(String scope) {
    this.scope = scope;
  }
}
