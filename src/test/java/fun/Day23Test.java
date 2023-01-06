package fun;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
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
public class Day23Test {

  private static final String FILE = "day23.txt";

  private List<Point> elves = new ArrayList<>();
  private int directionStart = 0;
  private int round = 0;
  private boolean oneMoved = true;

  private void readInput() throws IOException {

    List<String> lines = FileUtils.fromResourcesAsList( FILE );
    int maxRow = lines.size();
    int maxColumn = lines.get( 0 ).length();
    for( int i = 0; i < maxRow; i++ ) {
      String l = lines.get( i );
      for( int j = 0; j < maxColumn; j++ )
        if( l.charAt( j ) == '#' )
          elves.add( new Point( j, i ) );
    }

  }

  private void round() {

    oneMoved = false;

    List<Point> elvesNextStep = new ArrayList<>();

    for( Point elf : elves ) {
      if( allDirectionsClear( elf ) ) {
        elvesNextStep.add( elf );
        continue;
      }
      if( !canMove( elf ) )
        elvesNextStep.add( elf );
      else {
        oneMoved = true;
        int oneSimilar = elvesNextStep.indexOf( to );
        if( oneSimilar == -1 )
          elvesNextStep.add( to );
        else {
          elvesNextStep.add( elf );
          elvesNextStep.get( oneSimilar ).setLocation( elves.get( oneSimilar ) );
        }
      }
    }

    elves = elvesNextStep;
    directionStart = ( directionStart + 1 ) % 4;

  }

  private boolean canMove( Point elf ) {

    // 0 : North, 1 : South, 2 : West, 3 : East
    switch( directionStart ) {
      case 0:
        return canNorth( elf ) || canSouth( elf ) || canWest( elf ) || canEast( elf );
      case 1:
        return canSouth( elf ) || canWest( elf ) || canEast( elf ) || canNorth( elf );
      case 2:
        return canWest( elf ) || canEast( elf ) || canNorth( elf ) || canSouth( elf );
      case 3:
        return canEast( elf ) || canNorth( elf ) || canSouth( elf ) || canWest( elf );
      default:
        throw new RuntimeException( ">>" + directionStart );
    }

  }

  private Point to;

  private boolean allDirectionsClear( Point elf ) {
    to = elf;
    return elves.stream().noneMatch(
        p -> ( Math.abs( p.x - elf.x ) == 1 && p.y == elf.y )
            || ( Math.abs( p.y - elf.y ) == 1 && p.x == elf.x )
            || ( Math.abs( p.y - elf.y ) == 1 && Math.abs( p.x - elf.x ) == 1 ) );
  }

  private boolean canNorth( Point elf ) {
    to = new Point( elf.x, elf.y - 1 );
    return !elves.contains( to )
        && !elves.contains( new Point( to.x - 1, to.y ) )
        && !elves.contains( new Point( to.x + 1, to.y ) );
  }

  private boolean canSouth( Point elf ) {
    to = new Point( elf.x, elf.y + 1 );
    return !elves.contains( to )
        && !elves.contains( new Point( to.x - 1, to.y ) )
        && !elves.contains( new Point( to.x + 1, to.y ) );
  }

  private boolean canWest( Point elf ) {
    to = new Point( elf.x - 1, elf.y );
    return !elves.contains( to )
        && !elves.contains( new Point( to.x, to.y - 1 ) )
        && !elves.contains( new Point( to.x, to.y + 1 ) );
  }

  private boolean canEast( Point elf ) {
    to = new Point( elf.x + 1, elf.y );
    return !elves.contains( to )
        && !elves.contains( new Point( to.x, to.y - 1 ) )
        && !elves.contains( new Point( to.x, to.y + 1 ) );
  }

  @Test
  public void part1() throws IOException {
    readInput();
    while( round++ < 10 )
      round();
    int xMax = elves.stream().mapToInt( p -> p.x ).max().orElse( 0 );
    int xMin = elves.stream().mapToInt( p -> p.x ).min().orElse( 0 );
    int yMax = elves.stream().mapToInt( p -> p.y ).max().orElse( 0 );
    int yMin = elves.stream().mapToInt( p -> p.y ).min().orElse( 0 );
    CP.print(
        "Day23, part1, smallest rectangle that contains the Elves after 10 rounds",
        Map.of( "empty_grounds", ( xMax - xMin + 1 ) * ( yMax - yMin + 1 ) - elves.size() ) );
  }

  /*
   * Slow, takes 30s to executes, TODO need to find a simplification or try to
   * use arrays instead of lists? (with initial size *2 of data minimum)
   */
  @Test
  public void part2() throws IOException {
    readInput();
    while( oneMoved ) {
      round();
      round++;
      // System.out.println( round );
    }
    CP.print(
        "Day23, part2, number of the first round where no Elf moves",
        Map.of( "number", round ) ); // 1023
  }

}
