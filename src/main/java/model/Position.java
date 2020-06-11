package model;

import java.util.ArrayList;
import java.util.List;

public class Position {
    private int x, y;
    public List<Position> route = new ArrayList<>();

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Position(Position pos){
        this.x = pos.x;
        this.y = pos.y;
        route = new ArrayList<>(pos.route);
    }

    public void addToRoute(Position step) {
        route.add(step);
    }

    public int routeLength() {
        if (route == null) return 0;
        else return route.size();
    }

    public List<Position> getRoute() {
        return route;
    }

    public void setRoute(List<Position> route) {
        this.route = new ArrayList<>(route);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Position getRouteLast() {
        return route.get(route.size() - 1);
    }

    public boolean inBounds(int line) {
        return x >= 0 && y >= 0 && x < line && y < line;
    }

    public Position add(Position pos) {
        return new Position(this.x + pos.x, this.y + pos.y);
    }

    public Position add(int x, int y) {
        Position result = new Position(this.x + x, this.y + y);
        result.route = new ArrayList<>(route);
        return result;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Position))
            return false;
        Position to = (Position) o;
        return to.x == x && to.y == y;
    }

    public Position positionCenter(Position pos) {
        return new Position((this.x + pos.x) / 2, (this.y + pos.y) / 2);
    }
}