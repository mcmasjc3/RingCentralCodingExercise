package mcmaster.ringcentral.decodejwt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

public class JwtAuthenticatorHs256 implements JwtAuthenticator {
  private static final Joiner JOINER = Joiner.on('.');

  @Override
  public boolean authenticate(List<String> jwtParts, String secret) {
    return getSignature(jwtParts, secret).equals(jwtParts.get(2));
  }

  @VisibleForTesting
  String getSignature(List<String> jwtParts, String secret) {
    byte[] signature;
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
      mac.init(secretKeySpec);
      signature = mac.doFinal(JOINER.join(jwtParts.get(0), jwtParts.get(1)).getBytes());
    } catch (Exception e) {
      throw new InvalidJwtException("Failed to calculate HmacSHA256", e);
    }
    return new String(Base64.getEncoder().encode(signature));
  }
}
