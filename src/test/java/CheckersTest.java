import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertFalse;

class CheckersTest {
private Board test = new Board(8, 3);

  @Test
  void test(){
    Figure white = test.get(2, 5);
    Figure black = test.get(5, 2);
    test.set(new Position(1, 4), white);
    test.get(2, 5).setEmpty();
    test.set(new Position(6, 3), black);
    test.get(5, 2).setEmpty();
    assertFalse(test.get(1, 4).isEmpty());
    assertFalse(test.get(6, 3).isEmpty());

    Figure whiteQueen = test.get(1, 0);
    whiteQueen.setWhite(); whiteQueen.setQueen();
    test.set(new Position(5, 4), whiteQueen);
    test.get(1, 0).setEmpty();
    Figure blackQueen = test.get(0, 7);
    blackQueen.setBlack(); blackQueen.setQueen();
    test.set(new Position(3, 4), blackQueen);
    test.get(0, 7).setEmpty();
    assertFalse(test.get(5, 4).isEmpty());
    assertFalse(test.get(3, 4).isEmpty());
  }
}
