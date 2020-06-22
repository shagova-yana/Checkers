package controller;

import model.Position;
import java.util.*;

public class Logic {

    // передает список позиций достижимых с данной клетки
     List<Position> getMoves(Position from) {
        List<Position> result;
        if (Controller.board.get(from).isQueen())
            result = getStrikesQueen(from);
        else result = getStrikes(from);

        final int[] shifts = new int[] {-1, 1};
        if (result.isEmpty() && !Controller.board.get(from).isEmpty()) {
            if (Controller.board.get(from).isQueen())
                for (int shiftX : shifts)
                    for (int shiftY : shifts) {
                        Position to = from.add(shiftX, shiftY);
                        while (to.inBounds(Controller.board.size()) && Controller.board.get(to).isEmpty()) {
                            result.add(to);
                            to = to.add(shiftX, shiftY);
                        }
                    }
            else for (int shift : shifts) {
                Position move;
                if (Controller.board.get(from).color()) move = from.add(shift, 1);
                else move = from.add(shift, -1);
                if (Controller.board.get(move) != null && Controller.board.get(move).isEmpty())
                    result.add(new Position(move));
            } }
        for (Position pos : result)
            pos.addToRoute(new Position(from));
        return result;
    }

    // возвращает список позиций ударов из позиции
     List<Position> getStrikes(Position from) {
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
                    if (to.inBounds(Controller.board.size()) && Controller.board.get(to).isEmpty() &&
                            !Controller.board.get(to.positionCenter(search.getFirst())).isEmpty() &&
                            Controller.board.get(from).color() !=
                                    Controller.board.get(to.positionCenter(search.getFirst())).color() &&
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
     List<Position> getStrikesQueen(Position from) {
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

                    while (pos.inBounds(Controller.board.size()) &&
                            (Controller.board.get(pos).isEmpty() ||
                                    (pos.add(dirX, dirY).inBounds(Controller.board.size()) &&
                                            Controller.board.get(pos.add(dirX, dirY)).isEmpty() &&
                                            Controller.board.get(from).color() != Controller.board.get(pos).color()))) {
                        if (!Controller.board.get(pos).isEmpty() && Controller.board.get(from).color()
                                != Controller.board.get(pos).color() && !pos.getRoute().contains(pos) &&
                                pos.add(dirX, dirY).inBounds(Controller.board.size()) &&
                                Controller.board.get(pos.add(dirX, dirY)).isEmpty()) {
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
     List<Position> longestAvailableMoves(int minDepth, boolean color) {
        List<Position> result = new ArrayList<>();
        for (int i = 0; i < Controller.board.size(); i++)
            for (int j = 0; j < Controller.board.size(); j++)
                if (!Controller.board.get(i, j).isEmpty() &&
                        Controller.board.get(i, j).color() == color) {
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
     void findQueen() {
        for (int y = 0; y < Controller.board.size(); y++) {
            if (!Controller.board.get(y, 0).isEmpty() && !Controller.board.get(y, 0).color())
                Controller.board.get(y, 0).setQueen();
            if (!Controller.board.get(y, Controller.board.size() - 1).isEmpty() &&
                    Controller.board.get(y, Controller.board.size() - 1).color())
                Controller.board.get(y, Controller.board.size() - 1).setQueen();
        }
    }

    //удаляет все позиции длина мартшрута которых, меньше самой длинной из списка
     List<Position> filterShorter(List<Position> route) {
        int maxDepth = route.isEmpty() ? 0 : route.get(route.size() - 1).routeLength();
        route.removeIf(pos -> pos.routeLength() != maxDepth);
        return route;
    }

}
