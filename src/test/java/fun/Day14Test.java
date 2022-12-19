package fun;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.FileUtils;
import fun.utils.InputUtils;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day14Test {

  private static final String FILE = "day14.txt";

  // if want to show figure, do:
  // Set<Point> filledRocks = new HashSet<>();
  // Set<Point> filledSands = new HashSet<>();
  // Set<Point> filled = combination of both above;

  private final Set<Point> filled = new HashSet<>();

  private int abyss = 0;

  private boolean reachedAbyss = false;

  private void readInput() throws IOException {

    Stream<String> stream = FileUtils.fromResourcesAsStream( FILE );
    Iterator<String> it = stream.iterator();
    while( it.hasNext() ) {
      String[] points = it.next().split( " -> " );
      for( int i = 0; i < points.length - 1; i++ ) {
        Point point1 = InputUtils.sToPoint( points[i] );
        Point point2 = InputUtils.sToPoint( points[i + 1] );

        if( point1.x == point2.x && point1.y != point2.y )
          for( int y = Math.min( point1.y, point2.y ); y <= Math.max( point1.y, point2.y ); y++ )
            filled.add( new Point( point1.x, y ) );

        if( point1.x != point2.x && point1.y == point2.y )
          for( int x = Math.min( point1.x, point2.x ); x <= Math.max( point1.x, point2.x ); x++ )
            filled.add( new Point( x, point1.y ) );

        abyss = Math.max( abyss, Math.max( point1.y, point2.y ) );
      }
    }

  }

  private boolean oneStep( Point sand, boolean isPart2 ) {

    Point bottom = new Point( sand.x, sand.y + 1 );
    Point bottomLeft = new Point( sand.x - 1, sand.y + 1 );
    Point bottomRight = new Point( sand.x + 1, sand.y + 1 );
    if( !filled.contains( bottom ) )
      sand.y++;
    else if( !filled.contains( bottomLeft ) )
      sand.translate( -1, 1 );
    else if( !filled.contains( bottomRight ) )
      sand.translate( 1, 1 );
    else if( filled.contains( bottom ) ) {
      if( isPart2 )
        reachedAbyss = !filled.add( sand );
      else
        filled.add( sand );
      return true;
    }

    if( isPart2 ) {
      if( sand.y == abyss + 1 )
        return filled.add( sand );
    } else {
      reachedAbyss = sand.y >= abyss;
      return reachedAbyss;
    }

    return false;

  }

  private int treat( boolean part2 ) throws IOException {

    readInput();

    int sand = 0;
    boolean end;
    while( !reachedAbyss ) {
      sand++;
      end = false;
      Point newSand = new Point( 500, 0 );
      while( !end )
        end = oneStep( newSand, part2 );
    }
    return sand - 1;

  }

  @Test
  public void part1() throws IOException {
    CP.print( "Day14, part1, reached abyss", Map.of( "sand units", treat( false ) ) );
  }

  @Test
  public void part2() throws IOException {
    CP.print(
        "Day14, part2, reached abyss -2 as infinite and reached top",
        Map.of( "sand units", treat( true ) ) );
  }

}
