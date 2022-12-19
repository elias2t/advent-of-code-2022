package fun;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.beans.MyRange;
import fun.utils.CP;
import fun.utils.FileUtils;
import fun.utils.InputUtils;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day15Test {

  private static final String FILE = "day15.txt";
  private static final int ROW_P1 = 2000000;
  private static final int LIMIT_P2 = 4000000;

  private final List<Point> sensors = new ArrayList<>();
  private final List<Point> beacons = new ArrayList<>();
  private List<MyRange> ranges = new ArrayList<>();

  private void readInput() throws IOException {

    Iterator<String> it = FileUtils.fromResourcesAsStream( FILE ).iterator();
    while( it.hasNext() ) {
      String[] points = it.next().replace( "Sensor at x=", "" )
          .replace( " closest beacon is at x=", "" ).replace( " y=", "" ).split( ":" );
      sensors.add( InputUtils.sToPoint( points[0] ) );
      beacons.add( InputUtils.sToPoint( points[1] ) );
    }

  }

  private int getDistance( Point sensor, Point beacon ) {
    return Math.abs( sensor.x - beacon.x ) + Math.abs( sensor.y - beacon.y );
  }

  private void fillByRow( Point sensor, Point beacon, int rowToCheck ) {

    int dxy = getDistance( sensor, beacon );
    int diff = Math.abs( rowToCheck - sensor.y );
    int segmentSize = dxy - diff;
    if( dxy >= diff )
      ranges.add( new MyRange( sensor.x - segmentSize, sensor.x + segmentSize ) );

  }

  private void setRanges( int row ) {

    for( int i = 0; i < sensors.size(); i++ )
      fillByRow( sensors.get( i ), beacons.get( i ), row );

    ranges.sort( MyRange::compareTo );

    boolean mergable = true;
    while( mergable ) {
      mergable = false;
      for( int j = 0; j < ranges.size() - 1; j++ )
        if( ranges.get( j ).isMergePossible( ranges.get( j + 1 ) ) ) {
          ranges.remove( j + 1 );
          j++;
          mergable = true;
        }
    }

  }

  @Test
  public void part1() throws IOException {

    readInput();
    setRanges( ROW_P1 );

    int count = 0; // count are the beacons positions on this row
    for( MyRange range : ranges )
      for( Point beacon : new HashSet<>( beacons ) )
        if( ROW_P1 == beacon.y && range.contains( beacon.x ) )
          count++;

    CP.print(
        "Day15, part1, positions cannot contain a beacon",
        Map.of( "number", ranges.stream().mapToInt( MyRange::distance ).sum() - count ) );

  }

  private long par2_1() {

    int x = -1;
    int row = 0;
    while( x < 0 && row < LIMIT_P2 ) {
      // get ranges for each y from 0 to LIMIT_P2
      // limit x between 0 and LIMIT_P2
      ranges = new ArrayList<>();
      setRanges( row );
      for( MyRange r : ranges ) {
        r.start = Math.max( 0, r.start );
        r.end = Math.min( LIMIT_P2, r.end );
      }
      // if more than one range => found the empty x.
      // supposing x is not at a border.
      if( ranges.size() > 1 )
        x = ranges.get( 0 ).end + 1;
      row++;
    }
    return (long) x * LIMIT_P2 + ( row - 1 );

  }

  @Test
  // total time around 963 ms.
  public void part2() throws IOException {
    readInput();
    CP.print( "Day15, part2, tuning", Map.of( "frequency", par2_1() ) );
  }

  // ----------------------- Trying something else -----------------------

  private void addIfInLimit( Set<Point> border, Point p ) {
    if( 0 <= p.x && p.x <= LIMIT_P2 && 0 <= p.y && p.y <= LIMIT_P2 )
      border.add( p );
  }

  private Set<Point> findBorder( Point center, int distance ) {

    Set<Point> border = new HashSet<>();
    for( int i = 0; i < distance; i++ ) {
      int reverse = distance - i;
      addIfInLimit( border, new Point( center.x + i, center.y + reverse ) );
      addIfInLimit( border, new Point( center.x - i, center.y + reverse ) );
      addIfInLimit( border, new Point( center.x + i, center.y - reverse ) );
      addIfInLimit( border, new Point( center.x - i, center.y - reverse ) );
    }
    return border;

  }

  private long part2_2() {

    Point found = null;

    List<Integer> distancesMax = new ArrayList<>();
    for( int i = 0; i < sensors.size(); i++ )
      distancesMax.add( getDistance( sensors.get( i ), beacons.get( i ) ) );

    for( int i = 0; i < sensors.size(); i++ ) {
      Set<Point> borderPlusOne = findBorder( sensors.get( i ), distancesMax.get( i ) + 1 );
      for( Point p : borderPlusOne ) {
        boolean isOutsideDistance = true;
        for( int j = 0; j < sensors.size(); j++ )
          if( getDistance( sensors.get( j ), p ) <= distancesMax.get( j ) )
            isOutsideDistance = false;
        if( isOutsideDistance ) {
          found = p;
          break;
        }
      }
      if( found != null )
        break;
    }

    assert found != null;
    return (long) found.x * LIMIT_P2 + ( found.y );

  }

  @Test
  // total time around 800 ms
  public void part2_other() throws IOException {
    readInput();
    CP.print( "Day15, part2_other, tuning", Map.of( "frequency", part2_2() ) );
  }

}
