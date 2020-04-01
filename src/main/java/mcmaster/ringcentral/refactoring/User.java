package mcmaster.ringcentral.refactoring;

class User {
  public static class Builder {
    private String first;
    private String last;
    private String email;
    private String accountId;

    private Builder() {
    }

    public String getFirst() {
      return first;
    }

    public Builder setFirst(String first) {
      this.first = first;
      return this;
    }

    public String getLast() {
      return last;
    }

    public Builder setLast(String last) {
      this.last = last;
      return this;
    }

    public String getEmail() {
      return email;
    }

    public Builder setEmail(String email) {
      this.email = email;
      return this;
    }

    public String getAccountId() {
      return accountId;
    }

    public Builder setAccountId(String accountId) {
      this.accountId = accountId;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }

  private final String first;
  private final String last;
  private final String email;
  private final String accountId;

  private User(Builder builder) {
    first = builder.getFirst();
    last = builder.getLast();
    email = builder.getEmail();
    accountId = builder.getAccountId();
  }

  public String getFirst() {
    return first;
  }

  public String getLast() {
    return last;
  }

  public String getEmail() {
    return email;
  }

  public String getAccountId() {
    return accountId;
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
