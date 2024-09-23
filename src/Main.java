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
    //Environment settings
    private static final double FRICTION = 0.86;//greater = smoother
    private static final double ACCELERATION = 0.86;//acceleration of bats
    private static final double RADIUS = 30;//the size of bats

    //moving data of player1
    private double AvelocityX = 0;
    private double AvelocityY = 0;
    private boolean AisMovingLeft = false;
    private boolean AisMovingRight = false;
    private boolean AisMovingUp = false;
    private boolean AisMovingDown = false;

    //moving data of player2
    private double BvelocityX = 0;
    private double BvelocityY = 0;
    private boolean BisMovingLeft = false;
    private boolean BisMovingRight = false;
    private boolean BisMovingUp = false;
    private boolean BisMovingDown = false;

    //moving data of hockey
    private double HveloX = 0;
    private double HveloY = 0;

    //scoreboard
    private int playerAsc = 0;
    private int playerBsc = 0;
    private boolean getScore = true;

    @Override
    public void start(Stage primaryStage) {
        //initializing
        Pane root = new Pane();//dashboard
        Scene scene = new Scene(root, 500, 800);//size
        Image backgroundImage = new Image("background.png");//background img
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bg = new Background(background);
        root.setBackground(bg);//set
        primaryStage.setResizable(false);//cannot change size

        //create players and hockey
        Circle playerA = new Circle(RADIUS, Color.BLUE);
        Circle playerB = new Circle(RADIUS, Color.RED);
        Circle hockey = new Circle(20, Color.SILVER);
        //initialize the position
        playerA.setCenterX(250);
        playerA.setCenterY(600);
        playerB.setCenterX(250);
        playerB.setCenterY(200);
        hockey.setCenterX(250);
        hockey.setCenterY(395);
        //move mechanism
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

            if (code == KeyCode.U) {//reset hockey
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

        //render players
        root.getChildren().add(playerA);
        root.getChildren().add(playerB);
        root.getChildren().add(hockey);

        //render animations
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                //acceleration
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

                //add friction
                AvelocityX *= FRICTION;
                AvelocityY *= FRICTION;
                BvelocityX *= FRICTION;
                BvelocityY *= FRICTION;
                HveloX *= 0.95;
                HveloY *= 0.95;

                //check motion
                double AnextX = playerA.getCenterX() + AvelocityX;
                double AnextY = playerA.getCenterY() + AvelocityY;

                double BnextX = playerB.getCenterX() + BvelocityX;
                double BnextY = playerB.getCenterY() + BvelocityY;

                double HnextX = hockey.getCenterX() + HveloX;
                double HnextY = hockey.getCenterY() + HveloY;

                //bound
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

                //position update
                playerA.setCenterX(playerA.getCenterX() + AvelocityX);
                playerA.setCenterY(playerA.getCenterY() + AvelocityY);
                playerB.setCenterX(playerB.getCenterX() + BvelocityX);
                playerB.setCenterY(playerB.getCenterY() + BvelocityY);
                hockey.setCenterX(hockey.getCenterX() + HveloX);
                hockey.setCenterY(hockey.getCenterY() + HveloY);

                //hit check
                double distanceA = Math.sqrt(Math.pow(playerA.getCenterX() - hockey.getCenterX(), 2) + Math.pow(playerA.getCenterY() - hockey.getCenterY(), 2));
                double distanceB = Math.sqrt(Math.pow(playerB.getCenterX()-hockey.getCenterX(), 2) + Math.pow(playerB.getCenterY()-hockey.getCenterY(), 2));

                if (distanceA <= RADIUS + hockey.getRadius()) {
                    //change angle for a
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
                    //change angle for b
                    double angle = Math.atan2(hockey.getCenterY() - playerB.getCenterY(), hockey.getCenterX() - playerB.getCenterX());
                    HveloX = BvelocityX * 3;
                    HveloY = BvelocityY * 3;
                    hockey.setCenterX(playerB.getCenterX() + (RADIUS + hockey.getRadius() + 1) * Math.cos(angle));
                    hockey.setCenterY(playerB.getCenterY() + (RADIUS + hockey.getRadius() + 1) * Math.sin(angle));
                    double speed = Math.sqrt(Math.pow(BvelocityX, 2) + Math.pow(BvelocityY, 2));
                    BvelocityX = -BvelocityX*2-1;
                    BvelocityY = -BvelocityY*2-1;
                }

                //score
                if (hockey.getCenterY() <= 0 && (hockey.getCenterX()>150 && hockey.getCenterX() < 350) && getScore) {
                    playerAsc++;
                    getScore = false;
                } else if (hockey.getCenterY() >= 800 && (hockey.getCenterX()>150 && hockey.getCenterX() < 350) && getScore) {
                    playerBsc++;
                    getScore = false;
                }

                //set output
                primaryStage.setTitle("Simple Hockey! |P1| " + playerAsc + " VS " + playerBsc + " |P2|");

            }
        }.start();

        //ui
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
