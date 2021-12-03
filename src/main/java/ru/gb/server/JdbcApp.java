package ru.gb.server;

import java.sql.*;

class JdbcApp {
//    private JDBCApp jdbcApp = new JDBCApp();

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement getUserNicknameStatement;
    private static PreparedStatement createUserStatement;

    synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            System.out.println("connected to: \"jdbc:sqlite:users.db\"");
            statement = connection.createStatement();
            createTable();
            createTable();
            infoUsers();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void createTable() throws SQLException {
        statement.executeUpdate("" +
                "create table if not exists users(" +
                " id integer primary key autoincrement," +
                " nick text," +
                " login text," +
                " password text" +
                ")" +
                "");
    }

    public static void prepareAllStatement() throws SQLException {
        createUserStatement = connection.prepareStatement("INSERT INTO users (login, password, nick) VALUES (?, ?, ?);"); // метод на будующее для регистрации
        getUserNicknameStatement = connection.prepareStatement("select nick from users where login = ? and password = ?;");
    }

    static void infoUsers() throws SQLException {                                                                           // метод выводит просто информацию для меня на чат никак не влияет
        final ResultSet rs = statement.executeQuery("select * from users");
        while (rs.next()) {
            final int ID = rs.getInt(1);
            final String NICK = rs.getString(2);
            final String LOGIN = rs.getString(3);
            final String PASSWORD = rs.getString(4);

            System.out.printf("%d__%s__%s__%s\n", ID, NICK, LOGIN, PASSWORD);
        }
    }

    public static String getUserNickname(String login, String password) {
        String nick = null;
        try {
            getUserNicknameStatement.setString(1, login);
            getUserNicknameStatement.setString(2, password);
            ResultSet rs = getUserNicknameStatement.executeQuery();
            if (rs.next()) {
                nick = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nick;
    }


    synchronized static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean createUser(String nick, String login, String password) {
        try {
            createUserStatement.setString(1, nick);
            createUserStatement.setString(2, login);
            createUserStatement.setString(3, password);
            createUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}


//    static void select(int id) throws SQLException {
//        try (final PreparedStatement ps = connection.prepareStatement("select * from users where id = ?")) {
//            ps.setInt(1, id);
//            final ResultSet selected = ps.executeQuery();
//            while (selected.next()) {
//                final int ID = selected.getInt(1);
//                final String NICK = selected.getString(2);
//                final String LOGIN = selected.getString(3);
//                final String PASSWORD = selected.getString(4);
//
//                System.out.printf("Selected: " + "id:%d   name:%s   login:%s   pass:%s\n", ID, NICK, LOGIN, PASSWORD);
//            }
//        }
//    }
