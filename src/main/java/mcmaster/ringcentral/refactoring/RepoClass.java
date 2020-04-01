package mcmaster.ringcentral.refactoring;

import java.util.ArrayList;
import java.util.List;

public class RepoClass {
  private final Session session;

  @AutoWired
  public RepoClass(Session session) {
    this.session = session;
  }

  public List<User> getUsers(String accountId) {
    // The "User" parameter looks wrong to me, but I don't know how to fix it.
    // I think it might need to be "User.class", but I don't know enough about
    // Hibernate to tell.  I can't find a query method in Session JavaDocs.
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
}
