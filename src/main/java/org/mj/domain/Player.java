package org.mj.domain;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.mj.tetris.App;

import java.util.Random;

public class Player {

//     총 7개의 테트리스 블럭이 존재함.
//     총 7개의 블럭에, 각 블럭이 가지고 있는 모양의 갯수와,
//     각 모양별로 블럭의 위치를 기억하고 있어야 하기 때문에
//     모양 X 개수 X 블럭위치의 3차원 배열로 만들어져 있습니다.
    private Point2D[][][] shape = new Point2D[7][][];
    // 현재 모양
    private int current = 0;
    // 현재 회전 상태
    private int rotate = 0;
    private int nowColor = 0;

    //    colorSet 은 블럭이 나올때마다 7가지 색상중에 랜덤한 색상이 선택되어 나오도록 만들어진 배열입니다.
    private Color[] colorSet = new Color[7];

    private Random rnd;

    //    x, y는 현재 플레이어가 조종하고 있는 블록의 위치를 나타냅니다.
    private int x = 5;
    private int y = 2;

    private Block[][] board;

//     마지막으로 board 배열은 Game에 있던 board 배열과 동일합니다.
//     생성자에서 이 배열을 받아 함께 저장해서
//     플레이어 객체에서도 board에 있는 값들을 참조할 수 있도록 할 것입니다.
    public Player(Block[][] board) {
        this.board = board;
        //작대기
        shape[0] = new Point2D[2][];
        shape[0][0] = getPointArray("0,-1:0,0:0,1:0,2");
        shape[0][1] = getPointArray("-1,0:0,0:1,0:2,0");
        //네모
        shape[1] = new Point2D[1][];
        shape[1][0] = getPointArray("0,0:1,0:0,1:1,1");
        //ㄴ
        shape[2] = new Point2D[4][];
        shape[2][0] = getPointArray("0,-2:0,-1:0,0:1,0");
        shape[2][1] = getPointArray("0,1:0,0:1,0:2,0");
        shape[2][2] = getPointArray("-1,0:0,0:0,1:0,2");
        shape[2][3] = getPointArray("-2,0:-1,0:0,0:0,-1");
        // 역 ㄴ
        shape[3] = new Point2D[4][];
        shape[3][0] = getPointArray("0,-2:0,-1:0,0:-1,0");
        shape[3][1] = getPointArray("0,-1:0,0:1,0:2,0");
        shape[3][2] = getPointArray("0,0:1,0:0,1:0,2");
        shape[3][3] = getPointArray("-2,0:-1,0:0,0:0,1");
        // _┌━
        shape[4] = new Point2D[2][];
        shape[4][0] = getPointArray("-1,0:0,0:0,1:1,1");
        shape[4][1] = getPointArray("0,1:0,0:1,0:1,-1");
        // ─┐_
        shape[5] = new Point2D[2][];
        shape[5][0] = getPointArray("-1,1:0,1:0,0:1,0");
        shape[5][1] = getPointArray("-1,-1:-1,0:0,0:0,1");
        // ㅗ
        shape[6] = new Point2D[4][];
        shape[6][0] = getPointArray("-1,0:0,0:0,1:1,0");
        shape[6][1] = getPointArray("0,1:0,0:0,-1:1,0");
        shape[6][2] = getPointArray("-1,0:0,0:0,-1:1,0");
        shape[6][3] = getPointArray("-1,0:0,0:0,1:0,-1");

        // 색상 넣기
        colorSet[0] = Color.ALICEBLUE;
        colorSet[1] = Color.AQUAMARINE;
        colorSet[2] = Color.BEIGE;
        colorSet[3] = Color.BLUEVIOLET;
        colorSet[4] = Color.CORAL;
        colorSet[5] = Color.CRIMSON;
        colorSet[6] = Color.DODGERBLUE;

        rnd = new Random();
        current = rnd.nextInt(shape.length);
        nowColor = rnd.nextInt(colorSet.length);

        draw(false);
    }

    private void draw(boolean remove) {
        //블럭을 판에서 표시해주거나 없애주는 매서드
        for (int i = 0; i < shape[current][rotate].length; i++) {
            int bx = (int) shape[current][rotate][i].getX() + x;
            int by = (int) shape[current][rotate][i].getY() + y;
            board[by][bx].setData(!remove, colorSet[nowColor]);
//            draw 매서드는 입력되는 매개변수에 따라 해당 블럭을 지울 것인지 새로 그릴 것인지를 결정합니다.
//            만약 false가 들어왔다면 새롭게 그려주게 되고, true가 들어왔다면 Data의 fill을 false로 변경해서
//            해당 블럭을 판에서 그려지지 않도록 합니다.
        }
    }

    public Point2D[] getPointArray(String pointStr) {
        // 0,-1:0,0:0,1:0,2 형식으로 데이터가 들어오면 해당 데이터를 Point 객체 배열로 변경해주는 매서드
        Point2D[] arr = new Point2D[4];
        String[] pointList = pointStr.split(":");
        for (int i = 0; i < pointList.length; i++) {
            String[] point = pointList[i].split(",");
            double x = Double.parseDouble(point[0]);
            double y = Double.parseDouble(point[1]);
            arr[i] = new Point2D(x, y);
        }
        return arr;
    }

//    keyHandler, move, down 매서드는 모두 키보드 입력을 받아
//    플레이어가 조정하는 블럭을 이동시키기 위한 매서드들입니다.
//    하지만 아직 move나 down매서드의 내용이 작성되어 있지 않기 때문에 이 코드를 작성했다고 해서 블럭이 움직이거나 하지는 않습니다.
//    코드를 보면 왼쪽키와 오른쪽키의 눌림여부 그리고 위쪽키의 눌림여부에 따라 각각 dx, dy, rot 3개의 변수를 할당해서
//    move매서드를 실행하게 됩니다.
    public void keyHandler(KeyEvent e) {
        // 키보드 입력을 처리
        int dx = 0, dy = 0;
        boolean rot = false;
        if (e.getCode() == KeyCode.LEFT) {
            dx -= 1;
        } else if (e.getCode() == KeyCode.RIGHT) {
            dx += 1;
        } else if (e.getCode() == KeyCode.UP) {
            rot = true;
        }

        move(dx, dy, rot); //이동

        //내려가는 로직은 별도로 관리
        if (e.getCode() == KeyCode.DOWN) {
            down();
        } else if (e.getCode() == KeyCode.SPACE) {
            while (!down()) {
                //do nothing
            }
        }
    }
//    이제 실행해보면 회전, 이동은 정상적으로 이루어집니다.
//    하지만 블럭이 화면을 벗어나는 이동이나 회전을 하게 되면 정상적으로 돌지 않게 됩니다.
//    또한 아직 down매서드는 만들지 않았기 때문에 아래로 내리는 동작은 할 수 없습니다.
//    현재 문제는 블럭이 화면을 벗어나거나 다른블럭과 겹치는 것을 체크할 수 있는 방법이 없다는 것입니다.
//    이를 위해 checkPossible이라는 매서드를 만들어둔 것입니다.
    private void move(int dx, int dy, boolean rot) {
        // 블록을 이동
        draw(true); //지우고
        x += dx;
        y += dy;
        if (rot)
            rotate = (rotate + 1) % shape[current].length; //모양 갯수만큼만 증가

//        checkPossible 추가
//        checkPossible을 통해 false가 나오게되면 원상복귀 되는 루틴이 들어가 있습니다.
        if (!checkPossible()) {
            x -= dx;
            y -= dy;
            if (rot)  //회전되었었다면 회전도 원상복귀
                rotate = rotate - 1 < 0 ? shape[current].length - 1 : rotate - 1; //하나 다시 빼주고
        }
        draw(false);
    }

//    블럭을 내리고 더이상 움직일 수 없을 경우에는 바닥에 닿은 것으로 판정하여
//    원래위치로 이동후에 새로운 블럭을 뽑아주도록 합니다.
//    또한 바닥에 닿았다는 이야기는 테트리스의 블럭들이 완성된 라인을 지우는 작업을 해줘야 한다는 뜻이므로
//    game의 checkLineStatus를 실행하도록 합니다. (아직 해당 매서드는 완성되지 않았습니다.)

    public boolean down() {
        // 블럭을 한 칸 아래로
        draw(true); //지우기
        y += 1;
        if (!checkPossible()) {
            y -= 1;
            draw(false); //내려놓은 블럭 다시 그려주기
            App.app.game.checkLineStatus(); //블럭을 내린후에는 현재 라인상태를 체크하도록 함.
            getNextBlock();
            draw(false); //이동후 그려주기
            return true; //종료시에는 true
        }
        draw(false); //이동후 그려주기
        return false; //종료되지 않은경우 false
    }

//    getNextBlock은 블럭을 사용하고 난뒤
//    다음 블럭을 가져오는 매서드로 새로운 블럭을 랜덤하게 뽑고
//    해당 회전과 x, y값을 초기화해주는 역할을 하게 됩니다.
    private void getNextBlock() {
        // 다음 블럭 가져와서 초기화
        current = rnd.nextInt(shape.length);
        nowColor = rnd.nextInt(colorSet.length);
        x = 5;
        y = 2;
        rotate = 0;
    }

//    블럭의 4개 요소 모두가 겹치는 부분없이 모두 배치가 가능한지를 체크하고
//    이중 하나라도 화면밖을 벗어나거나 겨치게되면 false를 반환하고 아니면 true를 반환하는 방식입니다.
//    위의 매서드는 현재 블럭의 상태와 회전상태에 따라
//    4개의 블럭 모두 화면밖을 나가거나 해당 블럭이 위치할 곳에 이미 블럭이 있는지를 검사하여
//    모두 통과했다면 true를 그렇지 않다면 false를 반환하고 있습니다.
//    이제 이 매서드를 활용하여 블럭이 이동이 불가능할 때는 이동명령이 먹지 않도록
//    move 매서드를 다음과 같이 변경합니다.
    private boolean checkPossible() {
        // 블럭의 이동이 가능한지 체크
        for (int i = 0; i < shape[current][rotate].length; i++) {
            int bx = (int) shape[current][rotate][i].getX() + x;
            int by = (int) shape[current][rotate][i].getY() + y;
            if (bx < 0 || by < 0 || bx >= 10 || by >= 20) return false;

            if (board[by][bx].getFill()) return false; //이미 그곳에 블럭이 존재하면
        }
        return true;
    }
}
