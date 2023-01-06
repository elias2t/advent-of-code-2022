package fun;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day24Test {

  private static final String FILE = "day24.txt";

  private final List<Blizzard> blizzards = new ArrayList<>();
  private Set<Point> possibleLocations = new HashSet<>();

  private int destX;
  private int destY;
  private boolean reachedDest = false;
  private int stepsCount = 0;
  private boolean reverse = false;

  private void readInput() throws IOException {

    List<String> lines = FileUtils.fromResourcesAsList( FILE );
    int maxRow = lines.size();
    int maxColumn = lines.get( 0 ).length();
    destX = maxColumn - 2;
    destY = maxRow - 1;
    for( int i = 0; i < maxRow; i++ ) {
      String l = lines.get( i );
      for( int j = 0; j < maxColumn; j++ ) {
        char c = l.charAt( j );
        if( c != '#' && c != '.' )
          blizzards.add( new Blizzard( j, i, c, maxColumn - 2, maxRow - 2 ) );
      }
    }
    possibleLocations.add( new Point( 1, 0 ) );

  }

  @AllArgsConstructor
  @ToString
  private static class Blizzard {

    public int x;

    public int y;

    public char d;

    public int maxX;

    public int maxY;

    public void move() {
      switch( d ) {
        case '^':
          y = y == 1 ? maxY : y - 1;
          break;
        case 'v':
          y = y == maxY ? 1 : y + 1;
          break;
        case '<':
          x = x == 1 ? maxX : x - 1;
          break;
        case '>':
          x = x == maxX ? 1 : x + 1;
          break;
        default:
          throw new RuntimeException();
      }

    }

    public boolean isAt( int x, int y ) {
      return this.x == x && this.y == y;
    }

  }

  private void round() {

    stepsCount++;
    blizzards.forEach( Blizzard::move );
    for( Point location : new ArrayList<>( possibleLocations ) ) {
      possibleLocations.remove( location );
      possibleLocations.addAll( surroundings( location ) );
    }
    possibleLocations.forEach( this::check );

  }

  private void check( Point s ) {
    if( reverse && s.x == 1 && s.y == 1 )
      reachedDest = true;
    if( !reverse && s.x == destX && s.y == destY - 1 )
      reachedDest = true;
  }

  private boolean blizzardsNotAt( int x, int y ) {
    return blizzards.stream().noneMatch( b -> b.isAt( x, y ) );
  }

  private List<Point> surroundings( Point s ) {
    int x = s.x;
    int y = s.y;
    List<Point> possibleMoves = new ArrayList<>();
    if( ( x == 1 && y == 0 ) || ( x == destX && y == destY ) )
      possibleMoves.add( new Point( x, y ) );
    if( blizzardsNotAt( x, y ) && y != 0 && y != destY )
      possibleMoves.add( new Point( x, y ) );
    if( x > 1 && blizzardsNotAt( x - 1, y ) && y != destY )
      possibleMoves.add( new Point( x - 1, y ) );
    if( y > 1 && blizzardsNotAt( x, y - 1 ) )
      possibleMoves.add( new Point( x, y - 1 ) );
    if( x < destX && blizzardsNotAt( x + 1, y ) && y != 0 )
      possibleMoves.add( new Point( x + 1, y ) );
    if( y < destY - 1 && blizzardsNotAt( x, y + 1 ) )
      possibleMoves.add( new Point( x, y + 1 ) );
    return possibleMoves;
  }

  @Test
  public void part1() throws IOException {
    readInput();
    while( !reachedDest )
      round();
    CP.print(
        "Day24, part1, fewest number of minutes required to avoid the blizzards and reach the goal",
        Map.of( "minutes", stepsCount + 1 ) );
  }

  @Test
  public void part2() throws IOException {
    readInput();
    // Goal
    while( !reachedDest )
      round();
    // Back
    reachedDest = false;
    reverse = true;
    possibleLocations = new HashSet<>( List.of( new Point( destX, destY ) ) );
    while( !reachedDest )
      round();
    // Goal
    reachedDest = false;
    reverse = false;
    possibleLocations = new HashSet<>( List.of( new Point( 1, 0 ) ) );
    while( !reachedDest )
      round();
    CP.print(
        "Day24, part2, fewest number of minutes required to avoid the blizzards and reach the goal/back/goal",
        Map.of( "minutes", stepsCount + 1 ) );
  }

}
