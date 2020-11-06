package org.mj.views;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import org.mj.tetris.App;
import org.mj.tetris.Game;

public class MainController {
    @FXML
    private Canvas gameCanvas;

    @FXML
    public void initialize() {
        System.out.println("메인 레이아웃 초기화");

//    위의 매서드는 FXML 상의 모든 컴포넌트가 로드가 완료된뒤에 실행되게 되는 매서드인 initialize이기 때문에
//    gameCanvas가 올바르게 생성되어 바인딩되고 난 이후에 game을 생성할 수 있게 됩니다.
//    (이 작업을 App.java에서 하게 되면 gameCanvas에 접근할 수도 없을 뿐더러 코드의 위치에 따라 올바르게 생성되지 않을 수도 있습니다.
        App.app.game = new Game(gameCanvas);
    }
}
