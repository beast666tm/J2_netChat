package ru.gb.server;

public class ServerRunner {
    public static void main(String[] args) {

        try {
            JdbcApp.connect();  // подключение к users.db
            JdbcApp.prepareAllStatement();
//            JdbcApp.createTable(); // создание таблицы SQL если нет
//            JdbcApp.infoUsers();  // просто инфа из базы
//            JdbcApp.select(3);  // выборка из баззы по id

            new ChatServer().run();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcApp.disconnect();
        }
    }

}
