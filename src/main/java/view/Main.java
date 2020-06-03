package view;

import controller.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private static Stage stage;

    public static int size = 120;
    public static int line = 8;
    public static int columns = 8;

    public static void main(String[] args) {
        launch(args);
    }

        @FXML
        private Button newGame;

        @FXML
        void initialize() {
            assert newGame != null : "fx:id=\"newGame\" was not injected: check your FXML file 'introduction.fxml'.";
        }
    /*private void drawMessage(GraphicsContext gc, String str,
                             double x, double y, double size, boolean color) {
        if (color) {
            gc.setFont(new Font("Arial Black", size));
            gc.setFill(Color.BLACK);
        }
        else {
            gc.setFont(new Font("Arial", size));
            gc.setFill(Color.WHITE);
        }
        gc.fillText(str, x, y);
    }*/

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Group root = new Group();
        Main.stage = primaryStage;
        FXMLLoader start = new FXMLLoader(getClass().getResource("introduction.fxml"));
        root.getChildren().add(start.load());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Русские шашки");
        stage.getIcons().add(new Image("icon.png"));
        stage.setResizable(false);

        newGame.setOnAction(event -> Introduction.startGame());



       /* Image start = new Image("ches.jpg");
        ImageView view = new ImageView(start);
        root.getChildren().add(view);
        Button newGame = new Button("НОВАЯ ИГРА");
        newGame.setStyle("-fx-font: 35 arial; -fx-background-color: peachpuff");
        root.getChildren().add(newGame);
        newGame.setOnAction(event -> {
            root.getChildren().remove(newGame);
            root.getChildren().remove(view);
        });*/


        /*Canvas canvas = new Canvas(line * size, (columns * size) + 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Controller board = new Controller(0.0, 50.0, 960.0, 0.1, 8, 3);
        board.draw(gc);
        if (!board.isOpponentSet())
            board.setOpponent();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        board.draw(gc);
        if (board.isOpponentSet()){
            drawMessage(gc, board.message(), 10.0, 32.0, 22.0, true);
        }

        scene.setOnMouseClicked(
                e -> {
                    if (!board.isOpponentSet())
                        return;
                    if (board.isGameOverDelayed()){
                        board.reset();
                        scene.setOnMouseClicked(event -> {
                            primaryStage.close();
                        });
                    }
                    if (board.someLegalPos())
                        board.attemptMove(board.decodeMouse(e.getX(), e.getY()));
                    else
                        board.highlightMoves(board.decodeMouse(e.getX(), e.getY()));

                    gc.clearRect(0, 0, gc.getCanvas().getWidth(),
                            gc.getCanvas().getHeight());
                    board.draw(gc);
                    if (board.isOpponentSet()) {
                        drawMessage(gc, board.message(), 10.0, 17.0, 20, true);
                    }
                });
        stage.show();*/
    }
}
