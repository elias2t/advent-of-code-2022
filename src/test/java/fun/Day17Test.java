package fun;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.FileUtils;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day17Test {

  private static final String FILE = "day17.txt";
  private static final int OFFSET = 2 + 1;
  private static final int WIDE = 7 + 1;
  private static final int SOURCE = 3 + 1;

  private static class Pointer {
    public int p = 0;
  }

  private final List<Boolean> right = new ArrayList<>();
  private List<Point> filled = new ArrayList<>();
  // axis x is the ground, axis y is the left wall

  private void readInput() throws IOException {
    FileUtils.fromResourcesAsStream( FILE )
        .forEach( l -> l.chars().forEach( c -> right.add( c == '>' ) ) );
  }

  private interface Shape {

    int getRLimit();

    boolean oneStep( boolean right, List<Point> filled, Pointer pointer );

    boolean canGoDown( List<Point> filled );

    List<Point> asPoints();

    default boolean containsAny( List<Point> points, List<Point> filled ) {
      for( Point p : points )
        if( filled.contains( p ) )
          return true;
      return false;
    }

    default void translate( Pointer pointer, List<Boolean> right, List<Point> filled ) {
      while( oneStep( right.get( pointer.p % right.size() ), filled, pointer ) );
    }

  }

  abstract static class Common implements Shape {

    public int lLimit = OFFSET;
    public int duLimit;

    public boolean oneStep( boolean right, List<Point> filled, Pointer pointer ) {

      pointer.p++;
      if( right ) {
        lLimit++;
        if( getRLimit() == WIDE || containsAny( asPoints(), filled ) )
          lLimit--;
      } else {
        lLimit--;
        if( lLimit == 0 || containsAny( asPoints(), filled ) )
          lLimit++;
      }

      if( duLimit > 1 && canGoDown( filled ) ) {
        duLimit--;
        return true;
      } else {
        filled.addAll( asPoints() );
        return false;
      }

    }

  }

  public static class Minus extends Common {

    public Minus( int highestRock ) {
      duLimit = highestRock + SOURCE;
    }

    @Override
    public int getRLimit() {
      return lLimit + 3;
    }

    @Override
    public boolean canGoDown( List<Point> filled ) {
      List<Point> downPoints = List.of(
          new Point( lLimit, duLimit - 1 ),
          new Point( lLimit + 1, duLimit - 1 ),
          new Point( lLimit + 2, duLimit - 1 ),
          new Point( lLimit + 3, duLimit - 1 ) );
      return !containsAny( downPoints, filled );
    }

    @Override
    public List<Point> asPoints() {
      return List.of(
          new Point( lLimit, duLimit ),
          new Point( lLimit + 1, duLimit ),
          new Point( lLimit + 2, duLimit ),
          new Point( lLimit + 3, duLimit ) );
    }

  }

  public static class Plus extends Common {

    public Plus( int highestRock ) {
      duLimit = highestRock + SOURCE;
    }

    @Override
    public int getRLimit() {
      return lLimit + 2;
    }

    @Override
    public boolean canGoDown( List<Point> filled ) {
      List<Point> downPoints = List.of(
          new Point( lLimit, duLimit ),
          new Point( lLimit + 1, duLimit - 1 ),
          new Point( lLimit + 2, duLimit ) );
      return !containsAny( downPoints, filled );
    }

    @Override
    public List<Point> asPoints() {
      return List.of(
          new Point( lLimit + 1, duLimit ),
          new Point( lLimit, duLimit + 1 ),
          new Point( lLimit + 1, duLimit + 1 ),
          new Point( lLimit + 2, duLimit + 1 ),
          new Point( lLimit + 1, duLimit + 2 ) );
    }

  }

  public static class Corner extends Common {

    public Corner( int highestRock ) {
      duLimit = highestRock + SOURCE;
    }

    @Override
    public int getRLimit() {
      return lLimit + 2;
    }

    @Override
    public boolean canGoDown( List<Point> filled ) {
      List<Point> downPoints = List.of(
          new Point( lLimit, duLimit - 1 ),
          new Point( lLimit + 1, duLimit - 1 ),
          new Point( lLimit + 2, duLimit - 1 ) );
      return !containsAny( downPoints, filled );
    }

    @Override
    public List<Point> asPoints() {
      return List.of(
          new Point( lLimit, duLimit ),
          new Point( lLimit + 1, duLimit ),
          new Point( lLimit + 2, duLimit ),
          new Point( lLimit + 2, duLimit + 1 ),
          new Point( lLimit + 2, duLimit + 2 ) );
    }

  }

  public static class Wall extends Common {

    public Wall( int highestRock ) {
      duLimit = highestRock + SOURCE;
    }

    @Override
    public int getRLimit() {
      return lLimit;
    }

    @Override
    public boolean canGoDown( List<Point> filled ) {
      List<Point> downPoints = List.of( new Point( lLimit, duLimit - 1 ) );
      return !containsAny( downPoints, filled );
    }

    @Override
    public List<Point> asPoints() {
      return List.of(
          new Point( lLimit, duLimit ),
          new Point( lLimit, duLimit + 1 ),
          new Point( lLimit, duLimit + 2 ),
          new Point( lLimit, duLimit + 3 ) );
    }

  }

  public static class Square extends Common {

    public Square( int highestRock ) {
      duLimit = highestRock + SOURCE;
    }

    @Override
    public int getRLimit() {
      return lLimit + 1;
    }

    @Override
    public boolean canGoDown( List<Point> filled ) {
      List<Point> downPoints =
          List.of( new Point( lLimit, duLimit - 1 ), new Point( lLimit + 1, duLimit - 1 ) );
      return !containsAny( downPoints, filled );
    }

    @Override
    public List<Point> asPoints() {
      return List.of(
          new Point( lLimit, duLimit ),
          new Point( lLimit + 1, duLimit ),
          new Point( lLimit, duLimit + 1 ),
          new Point( lLimit + 1, duLimit + 1 ) );
    }

  }

  private Shape nextShape( int i ) {
    switch( i % 5 ) {
      case 0:
        return new Minus( getHighestRock() );
      case 1:
        return new Plus( getHighestRock() );
      case 2:
        return new Corner( getHighestRock() );
      case 3:
        return new Wall( getHighestRock() );
      case 4:
        return new Square( getHighestRock() );
      default:
        throw new RuntimeException();
    }
  }

  private int getHighestRock() {
    return filled.stream().mapToInt( p -> p.y ).max().orElse( 0 );
  }

  public int treat( int rocksNumber ) {
    int rock = 0;
    Pointer pointer = new Pointer();
    while( rock < rocksNumber )
      nextShape( rock++ ).translate( pointer, right, filled );
    return getHighestRock();
  }

  @Test
  public void part1() throws IOException {
    readInput();
    CP.print(
        "Day17, part1, tower of rocks be after 2022 rocks have fell",
        Map.of( "units", treat( 2022 ) ) );
  }

  public long treat2( long size ) {

    // Can't divide the size into many loops, because it still is too big.

    int rock = 0;
    Pointer pointer = new Pointer();
    List<String> fingerPrints = new ArrayList<>();
    List<Integer> highestByRock = new ArrayList<>();
    int rockI = 0;
    int rockJ = 0;
    while( rockJ == 0 ) {
      nextShape( rock ).translate( pointer, right, filled );

      int[] values = new int[7];
      for( int j = 1; j < 8; j++ ) {
        int finalJ = j;
        values[j - 1] =
            filled.stream().filter( p -> p.x == finalJ ).mapToInt( p -> p.y ).max().orElse( 0 );
      }
      highestByRock.add( Arrays.stream( values ).max().orElse( 0 ) );

      // shape + pointerLocation + form of last surface
      String asStrId =
          nextShape( rock ).getClass().getSimpleName() + ( pointer.p % right.size() ) + ",";
      final int min = Arrays.stream( values ).min().orElse( 0 );
      OfInt it = Arrays.stream( values ).map( v -> v - min ).iterator();
      while( it.hasNext() )
        asStrId += it.next();

      // compare
      int firstOccurrence = fingerPrints.indexOf( asStrId );
      if( firstOccurrence > 0 ) {
        rockI = firstOccurrence;
        rockJ = rock;
      }
      fingerPrints.add( asStrId );
      rock++;
    }

    // Amplify
    int diff = rockJ - rockI;
    int toMultiply = highestByRock.get( rockJ ) - highestByRock.get( rockI );
    long nTimes = ( size - 1 - rockI ) / diff;
    int rest = (int) ( ( size - 1 - rockI ) % diff );
    return nTimes * toMultiply + highestByRock.get( rockI + rest );

  }

  @Test
  public void part2() throws IOException {
    readInput();
    CP.print(
        "Day17, part2, tower of rocks be after 1000000000000L rocks have fell",
        Map.of( "units", treat2( 1000000000000L ) ) );
  }

}
