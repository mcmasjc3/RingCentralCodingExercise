package mcmaster.ringcentral.refactoring;

import java.util.List;

public class Service {
  @Autowired public RepoClass repo;

  public Service() {}

  public User addUser(String firstName, String lastName, String email, String accountId) {
    return User.newBuilder()
        .setFirstName(firstName)
        .setLastName(lastName)
        .setEmail(email)
        .setAccountId(accountId)
        .build();
  }

  public List<User> saveUsers(List<User> users) {
    return repo.saveUsers(users);
  }
}
