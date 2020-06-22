package controller;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.*;
import java.util.*;
import java.util.List;

public class Controller extends Logic {
    private final double pieceMargin, line, columns;
    private final double  startX, startY;
    protected static Board board;
    private List<Position> legalPos; // лист активных возможных позиций для хода
    boolean lastColor;
    boolean gameOver;
    boolean opponentSet;


    public Controller(double startX, double startY, double sideLength,
                      double pieceMargin, int sideCount, int startCount) {
        this.startX = startX;
        this.startY = startY;
        this.line= sideLength;
        this.pieceMargin = pieceMargin;
        this.columns = sideLength / sideCount;
        board = new Board(sideCount, startCount);
        this.legalPos = new ArrayList<>();
        this.lastColor = true;
        this.gameOver = false;
        this.opponentSet = false;
    }

//    public void reset() {
//        board.reset();
//        this.lastColor = true;
//        this.gameOver = false;
//        this.opponentSet = false;
//    }

    // подсвечивает все возможные ходы
    public void highlightMoves(Position from) {
        List<Position> longest = longestAvailableMoves(2, !lastColor);
        if (longest != null && from != null)
            if (longest.isEmpty() && from.inBounds(board.size()) &&
                    !board.get(from).isEmpty() && board.get(from).color() != lastColor)
                legalPos = getMoves(from);
            else for (Position strike : longest)
                legalPos.addAll(getMoves(strike));
    }

    // попытка совершить ход на данную позицию, если она содержиться в возможных
    public void attemptMove(Position to) {
        if (legalPos.contains(to)) {
            lastColor = !lastColor;
            board.set(to, board.get(legalPos.get(legalPos.indexOf(to)).getRouteLast()));
            for (Position step : legalPos.get(legalPos.indexOf(to)).getRoute())
                board.get(step).setEmpty();
            findQueen();
        }
        legalPos.clear();
    }

    public void drawFigure(Figure figure, GraphicsContext gc, double x, double y, double margin,
                           double line) {
        if (figure.isEmpty())
            return;
        if (figure.color())
            gc.setFill(Color.BLACK);
        else gc.setFill(Color.WHITE);

        gc.fillOval(x + margin * line, y + margin * line,
                (1 - 2 * margin) * line, (1 - 2 * margin) * line);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(x + margin * line, y + margin * line,
                (1 - 2 * margin) * line, (1 - 2 * margin) * line);
        if (figure.color())
            gc.setStroke(Color.WHITE);
        if (figure.isQueen())
            gc.strokeOval(x + 2*margin * line, y + 2*margin * line,
                    (1 - 4*margin) * line, (1 - 4*margin) * line);
    }

        public void draw(GraphicsContext gc) {
            // доска
            for (int i = 0; i < board.size(); i++)
                for (int j = 0; j < board.size(); j++) {
                    if ((i + j) % 2 == 0) gc.setFill(Color.MOCCASIN);
                    else gc.setFill(Color.SADDLEBROWN);
                    gc.fillRect(startX + j * columns, startY + i * columns,
                            columns, columns);
                }
            gc.setStroke(Color.BLACK);
            gc.strokeRect(startX, startY, line, line);
            // подсвеченные возможные ходы
            for (Position pos : legalPos) {
                gc.setFill(Color.INDIANRED);
                gc.fillRect(startX + pos.getX() * columns,
                        startY + pos.getY() * columns, columns, columns);
                gc.setFill(Color.LIGHTPINK);
                if (pos.route != null)
                    for (Position step : pos.route)
                        gc.fillRect(startX + step.getX() * columns,
                                startY + step.getY() * columns, columns, columns);
            }
            // шашки
            for (int i = 0; i < board.size(); i++)
                for (int j = 0; j < board.size(); j++)
                    drawFigure(board.get(i, j), gc, startX + i * columns,
                            startY + j * columns, pieceMargin, columns);
        }

                 // игра окончена?
    private boolean isGameOver() {
        gameOver = longestAvailableMoves(1, true).isEmpty() ||
                longestAvailableMoves(1, false).isEmpty();
        return gameOver;
    }

    public String message() {
        if (!longestAvailableMoves(2, !lastColor).isEmpty()) {
            return "Сейчас ходят: " + (lastColor ? "Белые" : "Черные") + "\nЖелательно рубить";
        }
        if (isGameOver())
            return "Игра окончена! Нажмите, чтобы закрыть";
        else return "Сейчас ходят: " + (lastColor ? "Белые" : "Черные");
    }

        // проверяет есть ли возможные позиции для хода
        public boolean someLegalPos() {
            return !legalPos.isEmpty();
        }

        public boolean isGameOverDelayed() {
            return gameOver;
        }

        // находит позиция доски, соответствующую мыши
        public Position decodeMouse(double mouseX, double mouseY) {
            if (mouseX > startX && mouseY > startY && mouseX < startX + line &&
                    mouseY < startY + line)
                return new Position( (int)((mouseX - startX) / columns),
                        (int)((mouseY - startY) / columns ));
            else return null;
        }

        public boolean isOpponentSet() {
            return opponentSet;
        }

        public void setOpponent() {
            opponentSet = true;
        }
}