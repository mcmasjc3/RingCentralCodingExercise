package mcmaster.ringcentral.refactoring;

class User {
  public static class Builder {
    private String firstName;
    private String lastName;
    private String email;
    private String accountId;

    private Builder() {}

    public String getFirstName() {
      return firstName;
    }

    public Builder setFirstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public String getLastName() {
      return lastName;
    }

    public Builder setLastName(String lastName) {
      this.lastName = lastName;
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

  private final String firstName;
  private final String lastName;
  private final String email;
  private final String accountId;

  private User(Builder builder) {
    firstName = builder.getFirstName();
    lastName = builder.getLastName();
    email = builder.getEmail();
    accountId = builder.getAccountId();
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
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
