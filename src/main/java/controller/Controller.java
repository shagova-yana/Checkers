package controller;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Board;
import model.Figure;
import model.Position;
import java.util.*;
import java.util.List;

public class Controller {
        private final double pieceMargin, line, columns;
        private double startX, startY;
        public Board board;
        private List<Position> legalPos; // лист активных возможных позиций для хода
        boolean lastColor, gameOver, opponentSet;


    public Controller(double startX, double startY, double sideLength,
                      double pieceMargin, int sideCount, int startCount) {
        this.startX = startX;
        this.startY = startY;
        this.line= sideLength;
        this.pieceMargin = pieceMargin;
        this.columns = sideLength / sideCount;
        this.board = new Board(sideCount, startCount);
        this.legalPos = new ArrayList<>();
        this.lastColor = true;
        this.gameOver = false;
        this.opponentSet = false;
    }

    public void reset() {
        board.reset();
        lastColor = true;
        gameOver = false;
        opponentSet = false;
    }

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

    // передает список позиций достижимых с данной клетки
    public List<Position> getMoves(Position from) {
        List<Position> result;
        if (board.get(from).isQueen())
            result = getStrikesQueen(from);
        else result = getStrikes(from);

        final int[] shifts = new int[] {-1, 1};
        if (result.isEmpty() && !board.get(from).isEmpty()) {
            if (board.get(from).isQueen())
                for (int shiftX : shifts)
                    for (int shiftY : shifts) {
                        Position to = from.add(shiftX, shiftY);
                        while (to.inBounds(board.size()) && board.get(to).isEmpty()) {
                            result.add(to);
                            to = to.add(shiftX, shiftY);
                        }
                    }
            else for (int shift : shifts) {
                Position move;
                if (board.get(from).color()) move = from.add(shift, 1);
                else move = from.add(shift, -1);
                if (board.get(move) != null && board.get(move).isEmpty())
                    result.add(new Position(move));
            } }
        for (Position pos : result)
            pos.addToRoute(new Position(from));
        return result;
    }

    // возвращает список позиций ударов из позиции
    private List<Position> getStrikes(Position from) {
        LinkedList<Position> search = new LinkedList<>();
        search.add(from);
        List<Position> result = new ArrayList<>();
        final int[] offsets = {-2, 2};

        while (!search.isEmpty()) {
            boolean finalPos = true;
            for (int offX : offsets)
                for (int offY : offsets) {
                    Position to = new Position(search.getFirst().getX() + offX,
                            search.getFirst().getY() + offY);
                    to.setRoute(search.getFirst().getRoute());
                    if (to.inBounds(board.size()) && board.get(to).isEmpty() &&
                            !board.get(to.positionCenter(search.getFirst())).isEmpty() &&
                            board.get(from).color() !=
                                    board.get(to.positionCenter(search.getFirst())).color() &&
                            !to.getRoute().contains(to.positionCenter(search.getFirst()))) {
                        to.addToRoute(new Position(to.positionCenter(search.getFirst())));
                        search.add(to);
                        finalPos = false;
                    }
                }
            if (finalPos && !search.getFirst().equals(from))
                result.add(search.getFirst());
            search.poll();
        }
        return filterShorter(result);
    }


    // список ударов для дамки
    private List<Position> getStrikesQueen(Position from) {
        LinkedList<Position> search = new LinkedList<>();
        search.add(from);
        List<Position> result = new ArrayList<>();
        final int[] direction = {-1, 1};

        while (!search.isEmpty()) {
            boolean finalPos = true;
            for (int dirX : direction)
                for (int dirY : direction) {
                    Position pos = search.getFirst().add(dirX, dirY);
                    Position strike = null;
                    pos.setRoute(new ArrayList<>(search.getFirst().getRoute()));

                    while (pos.inBounds(board.size()) &&
                            (board.get(pos).isEmpty() ||
                                    (pos.add(dirX, dirY).inBounds(board.size()) &&
                                            board.get(pos.add(dirX, dirY)).isEmpty() &&
                                            board.get(from).color() != board.get(pos).color()))) {
                        if (!board.get(pos).isEmpty() && board.get(from).color()
                                != board.get(pos).color() && !pos.getRoute().contains(pos) &&
                                pos.add(dirX, dirY).inBounds(board.size()) &&
                                board.get(pos.add(dirX, dirY)).isEmpty()) {
                            strike = new Position(pos);
                            finalPos = false;
                            pos = pos.add(dirX, dirY);
                            pos.addToRoute(strike);
                        }
                        if (strike != null && !pos.equals(strike))
                            search.add(pos);
                        pos = pos.add(dirX, dirY);
                    }
                }
            if (finalPos && !search.getFirst().equals(from))
                result.add(search.peek());
            search.poll();
        }
        return filterShorter(result);
    }

    //проверка и возвращение самые длинные возможные ходы для данного цвета
    public List<Position> longestAvailableMoves(int minDepth, boolean color) {
        List<Position> result = new ArrayList<>();
        for (int i = 0; i < board.size(); i++)
            for (int j = 0; j < board.size(); j++)
                if (!board.get(i, j).isEmpty() &&
                        board.get(i, j).color() == color) {
                    List<Position> legalPos = getMoves(new Position(i, j));
                    if (!legalPos.isEmpty()) {
                        if (legalPos.get(0).routeLength() > minDepth) {
                            result.clear();
                            minDepth = legalPos.get(0).routeLength();
                        }
                        if (legalPos.get(0).routeLength() == minDepth)
                            result.add(new Position(i, j));
                    }
                }
        return result;
    }

    // поиск и проверка дамок
    private void findQueen() {
        for (int y = 0; y < board.size(); y++) {
            if (!board.get(y, 0).isEmpty() && !board.get(y, 0).color())
                board.get(y, 0).setQueen();
            if (!board.get(y, board.size() - 1).isEmpty() &&
                    board.get(y, board.size() - 1).color())
                board.get(y, board.size() - 1).setQueen();
        }
    }

    //удаляет все позиции длина мартшрута которых, меньше самой длинной из списка
    private List<Position> filterShorter(List<Position> route) {
        int maxDepth = route.isEmpty() ? 0 : route.get(route.size() - 1).routeLength();
        route.removeIf(pos -> pos.routeLength() != maxDepth);
        return route;
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