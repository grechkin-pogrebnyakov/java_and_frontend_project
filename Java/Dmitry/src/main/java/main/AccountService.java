package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitry on 013 13.09.14.
 */
public class AccountService {
    private Map<String, UserProfile> users = new HashMap<String, UserProfile>();
    private Map<String, UserProfile> sessions = new HashMap<String, UserProfile>();

    public AccountService() {
        addUser(new UserProfile("admin","admin","admin@admin.ru"));
        addUser(new UserProfile("dmitr","","sorokin.dmitr@yandex.ru"));
    }

    public void addUser(UserProfile user) {
        this.users.put(user.getLogin(), user);
    }

    public void addSession(String id, String login) {
        this.sessions.put(id, users.get(login));
    }

    public int getCountOfUsers() {
        return this.users.size();
    }

    public int getCountOfSessions() {
        return this.sessions.size();
    }

    public void deleteSession(String id) {
        this.sessions.remove(id);
    }

    public boolean sessionsContainsKey(String keyId) {
        return  sessions.containsKey(keyId);
    }

    public boolean usersContainsKey(String keyId) {
        return  users.containsKey(keyId);
    }

    public String getPasswordByLogin(String login) {
        return users.get(login).getPassword();
    }

    public UserProfile getUserProfileByLogin(String login) {
        return users.get(login);
    }

    public UserProfile getUserProfileBySessionId(String id) {
        return sessions.get(id);
    }
}
