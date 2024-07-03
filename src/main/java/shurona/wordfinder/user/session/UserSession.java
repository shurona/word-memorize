package shurona.wordfinder.user.session;

import shurona.wordfinder.user.domain.UserRole;

public class UserSession {
    private Long userId;
    private UserRole role;

    public Long getUserId() {
        return userId;
    }

    public UserRole getRole() {
        return role;
    }

    public static UserSession createUserSession(Long userId, UserRole role) {
        UserSession userSession = new UserSession();
        userSession.userId = userId;
        userSession.role = role;
        return userSession;
    }
}
