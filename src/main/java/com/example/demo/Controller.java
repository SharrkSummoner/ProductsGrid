package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller {

    private int countViewProducts = 6; // Количество одновременно отоборажаем продуктов

    private int offsetProducts = 0; // Количечтво ранее отобразившихся продуктов

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField main_field;

    @FXML
    private TilePane productsPane;

    @FXML
    private Button buttonPrev;

    @FXML
    private Button buttonNext;

    // Объект на основен нашего класса для работы с БД
    DB db = null;

    @FXML
    void initialize() {

        // Инициируем объект
        db = new DB();
        // Вызов метод для подгрузки заданий внутрь программы
        loadInfo();
    }

    // Метод для подгрузки заданий внутрь программы
    void loadInfo() {
        try {
            // Удаляем прошлые данные
            productsPane.getChildren().clear();
            // Получаем новые данные из базы данных
            ArrayList<String[]> products = db.getProducts(countViewProducts, offsetProducts);
            for (int i = 0; i < products.size(); i++) { // Перебираем все через цикл
                String title = products.get(i)[0]; // Объявляем название
                int cost = Integer.parseInt(products.get(i)[1]); // Объявляем цену
                boolean isActive; // Объявляем состояние активности
                if (products.get(i)[2].equals("1")) // Если оно активно (равно = 1)
                    isActive = true; // Возвраещем true
                else
                    isActive = false; // Если неактивно, то false
                // Вызов метода getProductPane, передаем данные названия, цены и статуса активности
                Pane productPane = getProductPane(title, cost, isActive);
                // Доабвляем данные в объект VBox (all_tasks)
                productsPane.getChildren().add(productPane);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения блока с продуктом
    public Pane getProductPane(String productTitle, int price, boolean isActive) {
        VBox productBox = new VBox();

        productBox.setPrefWidth(200); // Задаем ширину
        productBox.setPrefHeight(200); // Задаем высоту
        productBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: white"); // Задаем стиль, вес и цвет
        productBox.setSpacing(5); // Отступы между объектами одного продукта
        productBox.setPadding(new Insets(5)); // Оступы от границ объект для данных внутри
        productBox.setAlignment(Pos.CENTER); // Центируем все объекты внутри

        // Задаем задний фон по умолчанию - белый с прозрачностью 10%
        productBox.setBackground(new Background(new BackgroundFill(
                Color.rgb(255, 255, 255, 0.1),
                CornerRadii.EMPTY,
                Insets.EMPTY)));

        // Добавляем иконку для товара
        ImageView icon = new ImageView();
        Image img = new Image(
                Objects.requireNonNull(Main.class.getResourceAsStream("producticon.jpg")),
                150,
                110,
                true,
                true);
        icon = new ImageView();
        icon.setImage(img);
        // Инициализируем ее в наш box
        productBox.getChildren().addAll(icon);

        // Создаем label описание товара
        Label titleLabel = new Label(productTitle);

        titleLabel.setTextAlignment(TextAlignment.CENTER); // Текст по центру
        titleLabel.setWrapText(true);
        titleLabel.setTextFill(Paint.valueOf("#FFF")); // Белый цвет текста

        productBox.getChildren().add(titleLabel); // Инициализируем описание в наш box

        // Создаем label для цены
        Label costLabel = new Label(price + " Рублей"); // Она будет состоять из цены и слова "Рублей"
        costLabel.setTextAlignment(TextAlignment.CENTER); // Текст по центру
        costLabel.setWrapText(true);
        costLabel.setTextFill(Paint.valueOf("#FFF")); // Белый цвет текста

        productBox.getChildren().add(costLabel); // Инициализируем цену в наш box

        // Создаем label для статуса активности
        Label isActiveLabel;
        // Если товар неактивен, то:
        if (!isActive) {
            isActiveLabel = new Label("неактивен"); // Отображаем состояние "неактивен"
            isActiveLabel.setTextAlignment(TextAlignment.CENTER); // Текст по центру
            isActiveLabel.setWrapText(true);
            isActiveLabel.setTextFill(Paint.valueOf("#FFF")); // Белый цвет текста

            productBox.getChildren().add(isActiveLabel); // Инициализируем статус активности

            // Меняем задний фон  - черный с прозрачностью 30%
            productBox.setBackground(new Background(new BackgroundFill(
                    Color.rgb(0, 0, 0, 0.3),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
        }
        return productBox;
    }

    // При нажатии на кнопку впере
    public void nextButtonClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        // Если вперед еще можно листать (есть данные), то:
        if (db.getProducts(countViewProducts, offsetProducts + countViewProducts).size() > 0)
            offsetProducts += countViewProducts;
        productsPane.getChildren().clear(); // Очищаем старые данные
        loadInfo(); // Загружаем новые
    }

    // При нажатии на кнпоку назад
    public void prevButtonClick(ActionEvent event) {
        // Если назад можно листать, то:
        if (offsetProducts - countViewProducts >= 0)
            offsetProducts -= countViewProducts;
        productsPane.getChildren().clear(); // Очищаем старые данные
        loadInfo(); // Загружаем новые
    }
}