package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Figure {
    private boolean empty, color, queen;

    public Figure() {
        empty = true;
        color = false;
        queen = false;
    }

    public Figure(Figure figure) {
        this.empty = figure.empty;
        this.color = figure.color;
        this.queen = figure.queen;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isQueen() {
        return queen;
    }

    public boolean color() {
        return color;
    }

    public void setWhite() {
        empty = false;
        queen = false;
        color = false;
    }

    public void setBlack() {
        empty = false;
        queen = false;
        color = true;
    }

    public void setQueen() {
        queen = true;
    }

    public void setEmpty() {
        empty = true;
    }

    public void draw(GraphicsContext gc, double x, double y, double margin,
                     double line) {
        if (empty)
            return;
        if (color)
            gc.setFill(Color.BLACK);
        else gc.setFill(Color.WHITE);

        gc.fillOval(x + margin * line, y + margin * line,
                (1 - 2 * margin) * line, (1 - 2 * margin) * line);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(x + margin * line, y + margin * line,
                (1 - 2 * margin) * line, (1 - 2 * margin) * line);
        if (color)
            gc.setStroke(Color.WHITE);
        if (queen)
            gc.strokeOval(x + 2*margin * line, y + 2*margin * line,
                    (1 - 4*margin) * line, (1 - 4*margin) * line);
    }
}