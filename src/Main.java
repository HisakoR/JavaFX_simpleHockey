import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {
    //环境数据
    private static final double FRICTION = 0.86;//摩擦力，控制球拍的速度和惯性
    private static final double ACCELERATION = 0.86;//加速度
    private static final double RADIUS = 30;//球拍大小

    //A操作数据
    private double AvelocityX = 0;
    private double AvelocityY = 0;
    private boolean AisMovingLeft = false;
    private boolean AisMovingRight = false;
    private boolean AisMovingUp = false;
    private boolean AisMovingDown = false;
    //A操作数据

    //B操作数据
    private double BvelocityX = 0;
    private double BvelocityY = 0;
    private boolean BisMovingLeft = false;
    private boolean BisMovingRight = false;
    private boolean BisMovingUp = false;
    private boolean BisMovingDown = false;
    //B操作数据

    //冰球操作数据
    private double HveloX = 0;
    private double HveloY = 0;
    //冰球操作数据

    //计分板
    private int playerAsc = 0;
    private int playerBsc = 0;
    private boolean getScore = true;

    //鼠标操作
    private double mouseX;
    private double mouseY;

    @Override
    public void start(Stage primaryStage) {
        //初始化程序
        Pane root = new Pane();//新操作面板
        Scene scene = new Scene(root, 500, 800);//大小
        Image backgroundImage = new Image("background.png");//背景
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bg = new Background(background);
        root.setBackground(bg);//设置背景
        primaryStage.setResizable(false);//不可调节大小

        //创建玩家A
        Circle playerA = new Circle(RADIUS, Color.BLUE);
        //创建玩家B
        Circle playerB = new Circle(RADIUS, Color.RED);
        //创建冰球
        Circle hockey = new Circle(20, Color.SILVER);
        //玩家A的初始坐标，在桌台下半部分
        playerA.setCenterX(250);
        playerA.setCenterY(600);
        //玩家B的初始坐标，在桌台上半部分
        playerB.setCenterX(250);
        playerB.setCenterY(200);
        //冰球的初始坐标
        hockey.setCenterX(250);
        hockey.setCenterY(395);
        //对于玩家的移动判定
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.LEFT) {
                AisMovingLeft = true;
            }
            else if (code == KeyCode.RIGHT) {
                AisMovingRight = true;
            }
            else if (code == KeyCode.UP) {
                AisMovingUp = true;
            }
            else if (code == KeyCode.DOWN) {
                AisMovingDown = true;
            }

            if (code == KeyCode.U) {//重置冰球按钮
                hockey.setCenterX(250);
                hockey.setCenterY(395);
                HveloY = 0;
                HveloX = 0;
                getScore = true;
            }

            if (code == KeyCode.A) {
                BisMovingLeft = true;
            }
            else if (code == KeyCode.D) {
                BisMovingRight = true;
            }
            else if (code == KeyCode.W) {
                BisMovingUp = true;
            }
            else if (code == KeyCode.S) {
                BisMovingDown = true;
            }
        });
        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.LEFT) {
                AisMovingLeft = false;
            }
            else if (code == KeyCode.RIGHT) {
                AisMovingRight = false;
            }
            else if (code == KeyCode.UP) {
                AisMovingUp = false;
            }
            else if (code == KeyCode.DOWN) {
                AisMovingDown = false;
            }

            if (code == KeyCode.A) {
                BisMovingLeft = false;
            }
            else if (code == KeyCode.D) {
                BisMovingRight = false;
            }
            else if (code == KeyCode.W) {
                BisMovingUp = false;
            }
            else if (code == KeyCode.S) {
                BisMovingDown = false;
            }
        });

        playerA.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        playerA.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;
            AvelocityX = deltaX * 4;
            AvelocityY = deltaY * 4;
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        //向场景添加并更新两个玩家
        root.getChildren().add(playerA);
        root.getChildren().add(playerB);
        root.getChildren().add(hockey);

        //动画渲染
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                //应用惯性系统
                if (AisMovingLeft) {
                    AvelocityX -= ACCELERATION;
                }
                if (AisMovingRight) {
                    AvelocityX += ACCELERATION;
                }
                if (AisMovingUp) {
                    AvelocityY -= ACCELERATION;
                }
                if (AisMovingDown) {
                    AvelocityY += ACCELERATION;
                }
                if (BisMovingLeft) {
                    BvelocityX -= ACCELERATION;
                }
                if (BisMovingRight) {
                    BvelocityX += ACCELERATION;
                }
                if (BisMovingUp) {
                    BvelocityY -= ACCELERATION;
                }
                if (BisMovingDown) {
                    BvelocityY += ACCELERATION;
                }

                //应用摩擦系数
                AvelocityX *= FRICTION;
                AvelocityY *= FRICTION;
                BvelocityX *= FRICTION;
                BvelocityY *= FRICTION;
                HveloX *= 0.95;
                HveloY *= 0.95;

                //检查球的运动轨迹
                double AnextX = playerA.getCenterX() + AvelocityX;
                double AnextY = playerA.getCenterY() + AvelocityY;

                double BnextX = playerB.getCenterX() + BvelocityX;
                double BnextY = playerB.getCenterY() + BvelocityY;

                double HnextX = hockey.getCenterX() + HveloX;
                double HnextY = hockey.getCenterY() + HveloY;

                //判断：如果球下一步会接触到屏幕边缘，将其反弹
                if (AnextX < RADIUS || AnextX > scene.getWidth() - RADIUS) {
                    AvelocityX = - AvelocityX;
                }
                if (AnextY < RADIUS || AnextY > scene.getHeight() - RADIUS|| AnextY > 800) {
                    AvelocityY = - AvelocityY;
                }

                if (BnextX < RADIUS || BnextX > scene.getWidth() - RADIUS) {
                    BvelocityX = - BvelocityX;
                }
                if (BnextY < RADIUS || BnextY > scene.getHeight() - RADIUS|| BnextY > 800) {
                    BvelocityY = - BvelocityY;
                }

                if (HnextX < 20 || HnextX > scene.getWidth() - 20) {
                    HveloX = - HveloX;
                }
                if ((HnextY < 20 && (HnextX < 150 || HnextX > 350)) || (HnextY > scene.getHeight() - 20 && (HnextX < 150 || HnextX > 350))|| HnextY > 800 && (HnextX < 150 || HnextX > 350)) {
                    HveloY = - HveloY;
                }

                //A更新位置
                playerA.setCenterX(playerA.getCenterX() + AvelocityX);
                playerA.setCenterY(playerA.getCenterY() + AvelocityY);
                //B更新位置
                playerB.setCenterX(playerB.getCenterX() + BvelocityX);
                playerB.setCenterY(playerB.getCenterY() + BvelocityY);
                //冰球更新位置
                hockey.setCenterX(hockey.getCenterX() + HveloX);
                hockey.setCenterY(hockey.getCenterY() + HveloY);

                //球拍与冰球的碰撞检测
                double distanceA = Math.sqrt(Math.pow(playerA.getCenterX() - hockey.getCenterX(), 2) + Math.pow(playerA.getCenterY() - hockey.getCenterY(), 2));
                double distanceB = Math.sqrt(Math.pow(playerB.getCenterX()-hockey.getCenterX(), 2) + Math.pow(playerB.getCenterY()-hockey.getCenterY(), 2));

                if (distanceA <= RADIUS + hockey.getRadius()) {
                    //碰撞发生，根据碰撞角度调整冰球速度
                    double angle = Math.atan2(hockey.getCenterY() - playerA.getCenterY(), hockey.getCenterX() - playerA.getCenterX());
                    HveloX = AvelocityX * 3;
                    HveloY = AvelocityY * 3;
                    hockey.setCenterX(playerA.getCenterX() + (RADIUS + hockey.getRadius() + 1) * Math.cos(angle));
                    hockey.setCenterY(playerA.getCenterY() + (RADIUS + hockey.getRadius() + 1) * Math.sin(angle));
                    double speed = Math.sqrt(Math.pow(AvelocityX, 2) + Math.pow(AvelocityY, 2));
                    AvelocityX = -AvelocityX*2-1;
                    AvelocityY = -AvelocityY*2-1;
                }

                if (distanceB <= RADIUS + hockey.getRadius()) {
                    //碰撞发生，根据碰撞角度调整冰球速度
                    double angle = Math.atan2(hockey.getCenterY() - playerB.getCenterY(), hockey.getCenterX() - playerB.getCenterX());
                    HveloX = BvelocityX * 3;
                    HveloY = BvelocityY * 3;
                    hockey.setCenterX(playerB.getCenterX() + (RADIUS + hockey.getRadius() + 1) * Math.cos(angle));
                    hockey.setCenterY(playerB.getCenterY() + (RADIUS + hockey.getRadius() + 1) * Math.sin(angle));
                    double speed = Math.sqrt(Math.pow(BvelocityX, 2) + Math.pow(BvelocityY, 2));
                    BvelocityX = -BvelocityX*2-1;
                    BvelocityY = -BvelocityY*2-1;
                }

                //得分判定
                if (hockey.getCenterY() <= 0 && (hockey.getCenterX()>150 && hockey.getCenterX() < 350) && getScore) { // 球进入A得分区
                    playerAsc++;
                    getScore = false;
                } else if (hockey.getCenterY() >= 800 && (hockey.getCenterX()>150 && hockey.getCenterX() < 350) && getScore) { // 球进入B得分区
                    playerBsc++;
                    getScore = false;
                }

                // 更新窗口标题显示得分信息
                primaryStage.setTitle("Simple Hockey! |P1| " + playerAsc + " VS " + playerBsc + " |P2|");

            }
        }.start();

        //界面内容
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //主要方法
    public static void main(String[] args) {
        launch(args);
    }
}
