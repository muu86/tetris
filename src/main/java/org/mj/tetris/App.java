package org.mj.tetris;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class App extends Application
{
//    싱글톤 기법을 활용하기 위한 스태틱 변수
//    우리가 만들 프로그램에서 Game 객체는 단 하나만 생성됩니다.
//    하나의 테트리스 어플리케이션에 2개의 게임 객체는 존재할 수 없습니다.
//    또한 이 Game객체는 대부분의 테트리스 모듈들에서 참조가 이뤄져야합니다.
//    예를 들어 플레이어는 키입력시 게임안에 있는 보드판을 참조해야합니다.
//    이런식으로 한개만 생성되며 다른 곳에서 참조가 빈번하게 이뤄질 경우 싱글톤 패턴으로 만드는 것이 편합니다.
    public static App app;
    public Game game = null;

    public static void main( String[] args )
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            //스태틱 변수에 자기자신을 넣어서 싱글톤으로 활용함.
            app = this;
            FXMLLoader loader = new FXMLLoader();

//            URL fxmlUrl = new File("src/main/java/org/mj/views/Main.fxml").toURL();

            loader.setLocation(getClass().getResource("/src/main/java/org/mj/views/Main.fxml"));
            AnchorPane anchorPane = (AnchorPane)loader.load();

            Scene scene = new Scene(anchorPane);

            scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                if (game != null) {
                    game.keyHandler(e);
                }
            });

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
