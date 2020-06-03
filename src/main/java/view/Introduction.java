package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controller.Controller;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Introduction {
    public static int size = 120;
    public static int line = 8;
    public static int columns = 8;

    private static void drawMessage(GraphicsContext gc, String str,
                                    double x, double y, double size) {
            gc.setFont(new Font("Arial Black", size));
            gc.setFill(Color.BLACK);
            gc.fillText(str, x, y);
    }

    public static void startGame(){
        Group root = new Group();
        Scene mainScene = new Scene(root);

        Stage mainWindow = new Stage();
        mainWindow.setTitle("Русские шашки");
        mainWindow.setScene(mainScene);
        mainWindow.getIcons().add(new Image("icon.png"));

        mainWindow.initModality(Modality.APPLICATION_MODAL);
        mainWindow.initOwner(Main.getStage());

        Canvas canvas = new Canvas(line * size, (columns * size) + 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Controller board = new Controller(0.0, 50.0, 960.0, 0.1, 8, 3);
        board.draw(gc);
        if (!board.isOpponentSet())
            board.setOpponent();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        board.draw(gc);
        if (board.isOpponentSet()){
            drawMessage(gc, board.message(), 10.0, 32.0, 22.0);
        }

        mainScene.setOnMouseClicked(
                e -> {
                    if (!board.isOpponentSet())
                        return;
                    if (board.isGameOverDelayed()){
                        board.reset();
                        mainScene.setOnMouseClicked(event -> {
                            mainWindow.close();
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
                        drawMessage(gc, board.message(), 10.0, 17.0, 20);
                    }
                });
        mainWindow.show();
    }
}
