package mcmaster.ringcentral.decodejwt;

public class JwtHeader {
  private String alg;
  private String typ;

  public String getAlg() {
    return alg;
  }

  public void setAlg(String alg) {
    this.alg = alg;
  }

  public String getTyp() {
    return typ;
  }

  public void setTyp(String typ) {
    this.typ = typ;
  }
}
