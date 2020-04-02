package mcmaster.ringcentral.decodejwt;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Base64;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static mcmaster.ringcentral.decodejwt.JwtParser.INVALID_JWT_HEADER;
import static mcmaster.ringcentral.decodejwt.JwtParser.INVALID_JWT_PAYLOAD;
import static mcmaster.ringcentral.decodejwt.JwtParser.UNABLE_TO_AUTHENTICATE_JWT;
import static mcmaster.ringcentral.decodejwt.JwtParser.UNKNOWN_AUTHENTICATION_ALGORITHM;
import static org.junit.Assert.assertThrows;

public class JwtParserTest {
  private static final String SECRET = "MySecret";

  private final JwtParser parser = new JwtParser();
  private final JwtAuthenticatorHs256 authenticator = new JwtAuthenticatorHs256();

  @Test
  public void parseInvalidHeader() {
    String header = "{ \"xxx\" : \"JWT\" }";
    String payload = "";
    String signature = "";
    String jwt = getEncodedJwt(header, payload, signature);
    InvalidJwtException expected =
        assertThrows(InvalidJwtException.class, () -> parser.parseJwt(jwt, SECRET));
    assertThat(expected.getMessage()).isEqualTo(String.format(INVALID_JWT_HEADER, header));
  }

  @Test
  public void parseUnknownAuthentication() {
    String header = "{ \"typ\" : \"JWT\", \"alg\" : \"xxx\" }";
    String payload = "";
    String signature = "";
    String jwt = getEncodedJwt(header, payload, signature);
    InvalidJwtException expected =
        assertThrows(InvalidJwtException.class, () -> parser.parseJwt(jwt, SECRET));
    assertThat(expected.getMessage())
        .isEqualTo(String.format(UNKNOWN_AUTHENTICATION_ALGORITHM, "xxx"));
  }

  @Test
  public void parseInvalidSignature() {
    String header = "{ \"typ\" : \"JWT\", \"alg\" : \"HS256\" }";
    String payload = "";
    String signature = "xxx";
    String jwt = getEncodedJwt(header, payload, signature);
    InvalidJwtException expected =
        assertThrows(InvalidJwtException.class, () -> parser.parseJwt(jwt, SECRET));
    assertThat(expected.getMessage()).isEqualTo(UNABLE_TO_AUTHENTICATE_JWT);
  }

  @Test
  public void parseInvalidPayload() {
    String header = "{ \"typ\" : \"JWT\", \"alg\" : \"HS256\" }";
    String payload = "{ \"name\" : \"Joe\", \"admin\" : \"true\", \"resource\" : \"payroll\"";
    String encodedHeader = new String(Base64.getEncoder().encode(header.getBytes()));
    String encodedPayload = new String(Base64.getEncoder().encode(payload.getBytes()));
    String signature =
        authenticator.getSignature(ImmutableList.of(encodedHeader, encodedPayload), SECRET);
    String jwt = getEncodedJwt(header, payload, signature);
    InvalidJwtException expected =
        assertThrows(InvalidJwtException.class, () -> parser.parseJwt(jwt, SECRET));
    assertThat(expected.getMessage()).isEqualTo(String.format(INVALID_JWT_PAYLOAD, payload));
  }

  @Test
  public void parsePayload() {
    String header = "{ \"typ\" : \"JWT\", \"alg\" : \"HS256\" }";
    String payload = "{ \"name\" : \"Joe\", \"admin\" : \"true\", \"resource\" : \"payroll\" }";
    String encodedHeader = new String(Base64.getEncoder().encode(header.getBytes()));
    String encodedPayload = new String(Base64.getEncoder().encode(payload.getBytes()));
    String signature =
        authenticator.getSignature(ImmutableList.of(encodedHeader, encodedPayload), SECRET);
    String jwt = getEncodedJwt(header, payload, signature);
    Map<String, String> payloadValues = parser.parseJwt(jwt, SECRET);
    assertThat(payloadValues)
        .containsExactly("name", "Joe", "admin", "true", "resource", "payroll");
  }

  private String getEncodedJwt(String header, String payload, String signature) {
    String encodedHeader = new String(Base64.getEncoder().encode(header.getBytes()));
    String encodedPayload = new String(Base64.getEncoder().encode(payload.getBytes()));
    return Joiner.on('.').join(encodedHeader, encodedPayload, signature);
  }
}
