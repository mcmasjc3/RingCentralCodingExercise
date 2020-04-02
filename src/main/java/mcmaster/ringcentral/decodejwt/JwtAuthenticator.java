package mcmaster.ringcentral.decodejwt;

import java.util.List;

public interface JwtAuthenticator {
  boolean authenticate(List<String> jwtParts, String secret);
}
