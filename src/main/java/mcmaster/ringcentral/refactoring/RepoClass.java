package mcmaster.ringcentral.refactoring;

import java.util.ArrayList;
import java.util.List;

public class RepoClass {
  private final Session session = HibernateUtil.getSession();

  public RepoClass() {}

  public List<User> getUsers(String accountId) {
    return session.query("SELECT User u where u.accountId = " + accountId, User);
  }

  public List<User> saveUsers(List<User> users) {
    List<User> result = new ArrayList<>();
    users.stream()
        .filter(user -> this.canAccessUsers(user.getAccountId()))
        .forEach(
            user -> {
              session.saveOrUpdate(user);
              result.add(user);
            });
    return result;
  }

  private Boolean canAccessUsers(String accountId) {
    UserAccess access = session.getUserAccess(accountId, SecurityContext.getCurrentUserId());
    return (access != null && access.permissions.indexOf("READ") > -1);
  }

  private void saveUser(User user) {
    session.saveOrUpdate(user);
  }
}
