package ru.gb.server;

public class SimpleAuthService implements AuthService {

//    private final List<UserData> users;

    public SimpleAuthService() {
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        return JdbcApp.getUserNickname(login, password);
    }

}
