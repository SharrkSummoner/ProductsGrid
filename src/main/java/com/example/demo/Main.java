package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Даем путь к нашему fxml файлу для отображания нужного макета
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("sample.fxml"));
        // Задаем иконку приложения
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("icon.jpg"))));
        // Загружаем сцену
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        // Отображаем
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}