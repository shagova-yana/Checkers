package model;

public class Board {
    private Figure[][] figure;
    private final int columns, line;

    public Board(int line, int startCount) {
        this.columns = startCount;
        this.line = line;
        figure = new Figure[line][line];
        for (int x = 0; x < figure.length; x++)
            for (int y = 0; y < figure[x].length; y++)
                figure[x][y] = new Figure();

        for (int j = 0; j < startCount; j++)
            for (int i = (j % 2 == 0) ? 1 : 0; i < line; i += 2) {
                figure[i][j].setBlack();
                figure[line - 1 - i][line - 1 - j].setWhite();
            }
    }

//    public void reset() {
//        for (int i = 0; i < figure.length; i++)
//            for (int j = 0; j < figure[i].length; j++)
//                figure[i][j].setEmpty();
//
//        for (int j = 0; j < columns; j++)
//            for (int i = (j % 2 == 0) ? 1 : 0; i < figure.length; i += 2) {
//                figure[i][j].setBlack();
//                figure[figure.length - 1 - i][figure.length - 1 - j].setWhite();
//            }
//    }

    public Figure get(Position pos) {
        if (pos.inBounds(figure.length))
            return figure[pos.getX()][pos.getY()];
        else return null;
    }

    public Figure get(int x, int y) {
        return get(new Position(x, y));
    }

    public int size() {
        return figure.length;
    }

    public void set(Position pos, Figure piece) {
        figure[pos.getX()][pos.getY()] = new Figure(piece);
    }
}