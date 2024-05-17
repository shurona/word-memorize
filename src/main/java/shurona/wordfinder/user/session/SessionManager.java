package shurona.wordfinder.user.session;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Component
public class SessionManager {
    private static class SessionUser {
        private Long userId;
        private Date createdDate;

        public SessionUser(Long userId, Date createdDate) {
            this.userId = userId;
            this.createdDate = createdDate;
        }

        public Long getUserId() {
            return userId;
        }

        public Date getCreatedDate() {
            return createdDate;
        }
    }

    private Map<String, Long> sessionStore = new ConcurrentHashMap<>();

    public void saveSessionWithUser(String sessionId, Long userId) {
        SessionUser sessionUser = new SessionUser(userId, new Date());

        this.sessionStore.put(sessionId, userId);
    }
}
