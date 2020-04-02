package mcmaster.ringcentral.decodejwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.util.Base64;
import java.util.List;
import java.util.Map;

public class JwtParser {
  @VisibleForTesting
  static final String INVALID_JWT_HEADER = "Invalid JWT Header: %s";
  @VisibleForTesting
  static final String UNKNOWN_AUTHENTICATION_ALGORITHM = "Unknown authentication algoritm %s";
  @VisibleForTesting
  static final String UNABLE_TO_AUTHENTICATE_JWT = "Unable to authenticate JWT";
  @VisibleForTesting
  static final String INVALID_JWT_PAYLOAD = "Invalid JWT Payload %s";

  private static final Splitter PART_SPLITTER = Splitter.on('.');
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final ImmutableMap<String, JwtAuthenticator> AUTHENTICATORS =
      ImmutableMap.<String, JwtAuthenticator>builder()
          .put("HS256", new JwtAuthenticatorHs256())
          // Add other authenticators as needed.
          .build();

  public Map<String, String> parseJwt(String jwt, String secret) {
    List<String> jwtParts = PART_SPLITTER.splitToList(jwt);
    String src = jwtParts.get(0);
    JwtHeader header = getHeader(src);
    authenticateJwt(jwtParts, secret, header.getAlg());
    return ImmutableMap.copyOf(getPayloadValues(jwtParts.get(1)));
  }

  private JwtHeader getHeader(String src) {
    String headerString = new String(Base64.getDecoder().decode(src));
    try {
      return OBJECT_MAPPER.readValue(headerString, JwtHeader.class);
    } catch (JsonProcessingException e) {
      throw new InvalidJwtException(String.format(INVALID_JWT_HEADER, headerString));
    }
  }

  private void authenticateJwt(List<String> jwtParts, String secret, String alg) {
    JwtAuthenticator authenticator = AUTHENTICATORS.get(alg);
    if (authenticator == null) {
      throw new InvalidJwtException(String.format(UNKNOWN_AUTHENTICATION_ALGORITHM, alg));
    }
    if (!authenticator.authenticate(jwtParts, secret)) {
      throw new InvalidJwtException(UNABLE_TO_AUTHENTICATE_JWT);
    }
  }

  private Map<String, String> getPayloadValues(String src) {
    Map<String, String> payloadvalues;
    String payloadString = new String(Base64.getDecoder().decode(src));
    try {
      payloadvalues = OBJECT_MAPPER.readValue(payloadString, new TypeReference<Map<String, String>>() {});
    } catch (JsonProcessingException e) {
      throw new InvalidJwtException(String.format(INVALID_JWT_PAYLOAD, payloadString));
    }
    return payloadvalues;
  }
}
