package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by serg on 13.09.14.
 */
public class AccountService {
    private Map <String, UserProfile> registeredUsers;
    private Map <String, UserProfile> users;

    public AccountService() {
        this.registeredUsers = new HashMap<String, UserProfile>();
        this.users = new HashMap<String, UserProfile>();
    }
    public boolean sign_in(String login, String password, String sessionId){
        UserProfile user = registeredUsers.getOrDefault(login, null);
        if(user == null){
            return false;
        } else {
            if (!users.containsKey(sessionId) && user.checkPassword(password)) {
                users.put(sessionId, user);
                return true;
            } else {
                return false;
            }
        }
    }
    public boolean sign_up(String login, String email, String password){
        UserProfile user = registeredUsers.getOrDefault(login, null);
        if(user == null){
            registeredUsers.put(login, new UserProfile(login, email, password));
            return true;
        } else {
            return  false;
        }
    }
    public boolean logOut(String sessionId){
        if(users.remove(sessionId) == null) {
            return false;
        } else {
            return true;
        }
    }
}
