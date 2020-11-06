package org.mj.tetris;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

import javafx.scene.paint.Color;
import org.mj.domain.Block;
import org.mj.domain.Player;

public class Game {
    private GraphicsContext gc;
    public Block[][] board;

    private double width;
    private double height;

    // AnimationTimer는 javafx에서 일정시간 간격으로 계속해서 작업을 실행해야할 때 쓰이는 쓰레드입니다.
    // 이 쓰레드에서 update와 render를 담당할 것입니다. 게임에서는 이것을 프레임이라고 합니다.
    private AnimationTimer mainLoop;

    // before변수는 이 mainLoop와 연관이 있습니다. 해당 프레임이 이전 프레임 실행 후
    // 몇 초후에 실행된 것인지를 판단하기 위해서는
    // 이전 프레임이 실행되었던 시간을 알고 있어야 합니다.
    // 이것을 저장할 변수가 바로 before입니다.
    private long before;

    // Player 형 변수 player는 게임의 플레이어에 관한 것들이 모여있습니다.
    // 여기서는 플레이어가 직접 조정하는 블럭을 의미합니다.
    private Player player;

    // blockDownTime은 블럭이 자동으로 내려올때까지 걸리는 시간입니다.
    // 이 시간이 0.5초에 도달하면 자동으로 블럭이 한칸씩 내려오게 됩니다.
    // 이 0.5초라는 시간은 고정이 아니라 게임을 진행하며 스코어가 올라갈 수록
    // 속도를 조정하는 작업을 해주어도 됩니다.
    private double blockDownTime = 0;

    // score 변수는 말그대로 점수 변수입니다.
    // 테트리스에서 한 줄을 없앨때마다 score가 1점씩 오르게 됩니다.
    private int score = 0;

    public Game(Canvas canvas) {
        // 캔버스의 너비와 높이를 가져온다
        width = canvas.getWidth();  // 404
        height = canvas.getHeight();    // 804


        double size = (width - 4) / 10;     // size 는 40

        board = new Block[20][10];

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new Block(j * size + 2, i * size + 2, size);
//                가로 4, 세로 4 비우고, x, y 좌표 지정 , size(40) 만큼 폭, 높이 잡아 블록 생성
            }
        }
        
        this.gc = canvas.getGraphicsContext2D();

//        애니메이션 타이머를 익명클래스 구현을 통해서 만들어지고 있습니다.
//        이 타이머는 start매서드를 걸어주는 순간부터 지속적으로 실행되며
//        실행시마다 자신의 실행시간을 nano시간단위로 입력해서 들어옵니다.
//        이를 통해 프로그래머는 이전 실행과 지금 실행간의 시간차를 구해서
//        프레임의 시간을 측정할 수 있게 됩니다.
//        여기서는 handle매서드에서 이전시간과의 격차를 측정한후
//        이를 이용해 update를 실행해주고 있고 그뒤에 render 매서드를 실행해주고 있습니다.
//        AnimationTimer는 인터페이스가 아닙니다.
//        start, stop 등의 다른 매서드도 가지고 있는 클래스입니다.
//        따라서 이를 람다식으로 만들 수는 없습니다.
        mainLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // now 는 나노초 단위로 들어옴
                update((now - before) / 1000000000d);
                before = now;
                render();
            }
        };

        before = System.nanoTime();

        player = new Player(board);
        mainLoop.start();
    }

//    업데이트 메서드
//    작성된 코드로 인해 이제 0.5초의 시간마다 블럭이 아래로 하강하게 됩니다.
//    delta시간은 위에서 AnimationTimer를 통해 넣었기 때문에 정상적으로 작동합니다.
    public void update(double delta) {
        // 매 프레임마다 실행되는 update 메서드
        // 블럭의 자동 하강 로직을 담당
        blockDownTime += delta; //0.5초마다 블럭을 아래로 내린다. 이 수치는 난이도 조절기능에서 조절 가능.
        if(blockDownTime >= 0.5) {
            player.down();
            blockDownTime = 0;
        }
    }

//    위의 매서드는 복잡해보이지만 기능적으로 따지면 꽤 간단합니다.
//    테트리스 스테이지의 가장 아랫쪽 라인부터 라인이 꽉차있는지를 검사하고(첫번째 for문)
//    해당줄이 꽉차 있다면 해당 라인을 전부 지운후에 위에 칸부터 해당라인까지 한칸씩 내려주는 역할을 합니다.
//    마지막으로 한줄을 지웠기 때문에 새롭게 내려온 라인도 지워야하는지 검사하기 위해서 i를 하나 더 증가시켜줍니다.
//    이렇게 라인을 지우고 스코어까지 올려주어서 게임의 기본적인 로직을 만들었습니다.
    public void checkLineStatus() {
        // 라인이 꽉 찼는지 체크해주는 메서드
        for(int i = 19; i >= 0; i--) { //맨 밑칸부터 검사하면서 올라간다.
            boolean clear = true;
            for(int j = 0; j < 10; j++) {
                if(!board[i][j].getFill()) {
                    clear = false; //한칸이라도 비어 있다면 클리어되지 않은 것으로.
                    break;
                }
            }
            if(clear) {//해당 줄이 꽉차 있다면
                score++;
                for(int j = 0; j < 10; j++) {
                    board[i][j].setData(false, Color.WHITE); //해당 줄 지우고
                }
                //그 위로 한칸씩 다 내린다.
                for(int k = i - 1;  k >= 0; k--) {
                    for(int j = 0; j < 10; j++) {
                        board[k+1][j].copyData(board[k][j]);
                    }
                }
                //첫번째 줄은 비운다.
                for(int j = 0; j < 10; j++) {
                    board[0][j].setData(false, Color.WHITE);
                }
                i++;//그리고 한번더 이번줄을 검사하기 위해 i값을 하나 증가시켜 준다.
            }
        }
    }

    public void render() {
        //매 프레임마다 화면을 그려주는 메서드
        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.rgb(0, 0, 0));
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, width, height);
//        "GraphicsContext".strokeRect(가로 위치, 세로 위치, 폭, 높이);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j].render(gc);
            }
        }
    }

    public void keyHandler(KeyEvent e) {
        //키보드 핸들링을 담당
        player.keyHandler(e);

    }
}
