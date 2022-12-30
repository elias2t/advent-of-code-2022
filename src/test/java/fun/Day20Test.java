package fun;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.FileUtils;
import lombok.Data;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day20Test {

  private static final String FILE = "day20.txt";

  private List<Item> values;

  private int size = 0;

  private void readInput() throws IOException {
    values = FileUtils.fromResourcesAsStream( FILE ).map( s -> new Item( s, ++size ) )
        .collect( Collectors.toList() );
    values.forEach( v -> v.setSize( size ) );
  }

  @Data
  public static class Item {

    public int oldP;

    public int currP;

    public long v;

    public int size;

    public Item( String s, int position ) {
      this.v = Integer.parseInt( s );
      this.oldP = position;
      this.currP = position;
    }

    public void setSize( int size ) {
      this.size = size;
    }

    public int decrement( int x ) {
      currP -= x;
      return currP;
    }

    public int increment( int x ) {
      currP += x;
      return currP;
    }

    public int move( int x ) {
      return x < 0 ? moveLeft( -x ) : moveRight( x );
    }

    public int moveLeft( int x ) {
      return x >= currP ? moveRight( size - currP - ( x - currP + 1 ) ) : decrement( x );
    }

    public int moveRight( int x ) {
      return x > size - currP ? moveLeft( currP - 1 - ( x - ( size - currP ) ) ) : increment( x );
    }

  }

  public long treat( boolean part2 ) {

    Item item0 = null;
    int round = part2 ? 10 : 1;
    if( part2 )
      values.forEach( v -> v.v *= 811589153 );

    // mixing
    for( int k = 0; k < round; k++ )
      for( int i = 0; i < size; i++ ) {
        Item item = values.get( i );
        if( item.v == 0 )
          item0 = item;
        else {
          item.move( (int) ( item.v % ( size - 1 ) ) );
          for( int j = 0; j < size; j++ )
            if( j != i ) {
              Item rel = values.get( j );
              if( rel.currP > item.oldP && rel.currP <= item.currP )
                rel.moveLeft( 1 );
              if( rel.currP < item.oldP && rel.currP >= item.currP )
                rel.moveRight( 1 );
            }
        }
        values.forEach( v -> v.oldP = v.currP );
      }

    // re-order by currP
    List<Item> sorted = values.stream().sorted( Comparator.comparingInt( v -> v.currP ) )
        .collect( Collectors.toList() );
    int index = sorted.indexOf( item0 );
    return sorted.get( ( index + 1000 ) % size ).v
        + sorted.get( ( index + 2000 ) % size ).v
        + sorted.get( ( index + 3000 ) % size ).v;

  }

  @Test
  public void part1() throws IOException {
    readInput();
    CP.print(
        "Day20, part1, three numbers that form the grove coordinates",
        Map.of( "sum", treat( false ) ) );
  }

  @Test
  public void part2() throws IOException {
    readInput();
    CP.print(
        "Day20, part2, three numbers that form the grove coordinates",
        Map.of( "sum", treat( true ) ) );
  }

}
