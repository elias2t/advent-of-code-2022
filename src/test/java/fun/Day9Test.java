package fun;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.FileUtils;
import lombok.Getter;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day9Test {

  private static final String FILE = "day9.txt";

  @Getter
  private static class Line {

    private final String direction; // R, L, U, D

    private final int steps;

    public Line( String line ) {
      String[] s = line.split( " " );
      direction = s[0];
      steps = Integer.parseInt( s[1] );
    }

  }

  private void oneStepMove(
      boolean isHead,
      String direction,
      Point headPosition,
      Point tailPosition ) {

    int unused;
    if( isHead )
      unused = direction.equals( "R" ) ? headPosition.x++
          : direction.equals( "L" ) ? headPosition.x--
              : direction.equals( "U" ) ? headPosition.y++ : headPosition.y--;

    int xDelta = headPosition.x - tailPosition.x;
    int yDelta = headPosition.y - tailPosition.y;

    if( Math.abs( xDelta ) == 2 && Math.abs( yDelta ) == 2 ) {
      tailPosition.x += xDelta > 0 ? 1 : -1;
      tailPosition.y += yDelta > 0 ? 1 : -1;
    } else if( Math.abs( xDelta ) == 2 ) {
      tailPosition.x += xDelta > 0 ? 1 : -1;
      if( Math.abs( yDelta ) == 1 )
        tailPosition.y += yDelta;
    } else if( Math.abs( yDelta ) == 2 ) {
      tailPosition.y += yDelta > 0 ? 1 : -1;
      if( Math.abs( xDelta ) == 1 )
        tailPosition.x += xDelta;
    }

  }

  private int treat( int knotNumber ) throws IOException {

    List<Point> knots = new ArrayList<>();
    for( int i = 0; i < knotNumber; i++ )
      knots.add( new Point() );
    Set<String> positionsVisitedByLastKnot = new HashSet<>();
    positionsVisitedByLastKnot.add( new Point().toString() );

    List<Line> lines =
        FileUtils.fromResourcesAsStream( FILE ).map( Line::new ).collect( Collectors.toList() );
    for( Line line : lines )
      for( int i = 0; i < line.getSteps(); i++ ) {
        for( int k = 0; k < knotNumber - 1; k++ )
          oneStepMove( k == 0, line.getDirection(), knots.get( k ), knots.get( k + 1 ) );
        positionsVisitedByLastKnot.add( knots.get( knotNumber - 1 ).toString() );
      }

    return positionsVisitedByLastKnot.size();

  }

  @Test
  public void part1() throws IOException {
    int result = treat( 2 );
    CP.print(
        "Day9, part1, tail visited unique positions",
        Map.of( "knot number", 2, "positions", result ) );
  }

  @Test
  public void part2() throws IOException {
    int result = treat( 10 );
    CP.print(
        "Day9, part2, tail visited unique positions",
        Map.of( "knot number", 10, "positions", result ) );
  }

}
