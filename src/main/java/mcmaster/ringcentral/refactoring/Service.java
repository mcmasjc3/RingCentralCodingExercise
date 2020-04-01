package mcmaster.ringcentral.refactoring;

import java.util.List;

public class Service {
  private final RepoClass repo;

  @AutoWired
  public Service(RepoClass repo) {
    this.repo = repo;
  }

  public User addUser(String first, String last, String email, String accountId) {
    return User.newBuilder()
        .setFirst(first)
        .setLast(last)
        .setEmail(email)
        .setAccountId(accountId)
        .build();
  }

  public List<User> saveUsers(List<User> users) {
    return repo.saveUsers(users);
  }
}
