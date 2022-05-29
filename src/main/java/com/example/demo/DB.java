package com.example.demo;

import java.sql.*;
import java.util.ArrayList;

public class DB {
    // Данные для подключения к локальной базе данных
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DB_NAME = "zachet";
    private final String LOGIN = "root";
    private final String PASS = "";
    private Connection dbConn = null;

    // Метод для подключения к БД с использованием значений выше
    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    // Метод для получения данных о продукте
    // На вход подается требуемое количество данных и с какого момента их нужно вернуть
    public ArrayList<String[]> getProducts(int limit, int offset) throws SQLException, ClassNotFoundException {
        // sql запрос, который возвращает все данные из таблицы products в количестве limit, начиная с offset
        String sql = "SELECT * FROM Product LIMIT " + limit + " OFFSET " + offset;

        // Отправляем запрос sql
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);

        // Полученный данные из столбцов title, cost и isActive записываем в переменные
        ArrayList<String[]> products = new ArrayList<String[]>();
        while (res.next()) {
            String title = res.getString("Title"); // Название продукта
            String cost = Integer.toString(res.getInt("Cost")); // Цена продукта
            String isActive = res.getString("isActive"); // Статус активности

            products.add(new String[]{title, cost, isActive}); // Записываем
        }
        return products;
    }
}
