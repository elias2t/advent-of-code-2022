package fun;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
public class Day12Test {

  private static final String FILE = "day12.txt";

  private int wide;
  private int height;
  private char[][] m = null;
  private int[][] stepsCount = null;
  private static final int BIG = 99999;

  private Point S = null;
  private Point E = null;
  private final LinkedList<Point> toTreat = new LinkedList<>();
  private final List<Point> anyA = new ArrayList<>();

  private void readInput() throws IOException {

    List<String> lines = FileUtils.fromResourcesAsList( FILE );
    wide = lines.get( 0 ).length(); // i
    height = lines.size(); // j
    m = new char[height][wide];
    stepsCount = new int[height][wide];

    for( int j = 0; j < height; j++ ) {
      String line = lines.get( j );
      for( int i = 0; i < wide; i++ ) {

        char c = line.charAt( i );
        if( c == 'S' ) {
          S = new Point( i, j );
          m[j][i] = 'a';
        } else if( c == 'E' ) {
          E = new Point( i, j );
          m[j][i] = 'z';
        } else
          m[j][i] = c;

        stepsCount[j][i] = BIG;
        if( c == 'a' || c == 'S' )
          anyA.add( new Point( i, j ) );

      }
    }

  }

  /**
   * If next location (p2) not reached yet "== BIG" or is reached but with
   * larger steps count, update it to new best steps count.
   */
  private void fill( Point p1, Point p2 ) {
    if( stepsCount[p2.y][p2.x] > stepsCount[p1.y][p1.x] + 1 ) {
      stepsCount[p2.y][p2.x] = stepsCount[p1.y][p1.x] + 1;
      toTreat.add( p2 );
    }
  }

  /** If you can move to a location. */
  private boolean possibleStep( char from, char to ) {
    return to <= from + 1;
  }

  /** Check surrounding locations. */
  private void check( Point p ) {
    int i = p.x;
    int j = p.y;
    if( i > 0 && possibleStep( m[j][i], m[j][i - 1] ) ) // left
      fill( p, new Point( i - 1, j ) );
    if( i < wide - 1 && possibleStep( m[j][i], m[j][i + 1] ) ) // right
      fill( p, new Point( i + 1, j ) );
    if( j > 0 && possibleStep( m[j][i], m[j - 1][i] ) ) // up
      fill( p, new Point( i, j - 1 ) );
    if( j < height - 1 && possibleStep( m[j][i], m[j + 1][i] ) ) // down
      fill( p, new Point( i, j + 1 ) );
  }

  private int reset( int passBall ) {
    for( int j = 0; j < height; j++ )
      for( int i = 0; i < wide; i++ )
        stepsCount[j][i] = BIG;
    return passBall;
  }

  private int find( Point localS ) {
    toTreat.add( localS );
    stepsCount[localS.y][localS.x] = 0;
    while( !toTreat.isEmpty() )
      check( toTreat.pollFirst() );
    return stepsCount[E.y][E.x];
  }

  @Test
  public void part1() throws IOException {
    readInput();
    CP.print(
        "Day12, part1, the fewest steps required to find the best signal location starting from S",
        Map.of( "steps", find( S ) ) );
  }

  @Test
  public void part2() throws IOException {
    readInput();
    int best = BIG;
    for( Point a : anyA )
      best = Math.min( reset( best ), find( a ) );
    CP.print(
        "Day12, part2, the fewest steps required to find the best signal location starting from any 'a'",
        Map.of( "steps", best ) );
  }

}
