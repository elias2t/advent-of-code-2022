package fun;

import java.io.BufferedReader;
import java.io.IOException;
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
public class Day5Test {

  private static final String FILE = "day5.txt";

  // Can't use Java Queue, the remove() does the reverse side needed, from HEAD
  // of queue and not the tail as required.
  List<Character> stack1 = new LinkedList<>();
  List<Character> stack2 = new LinkedList<>();
  List<Character> stack3 = new LinkedList<>();
  List<Character> stack4 = new LinkedList<>();
  List<Character> stack5 = new LinkedList<>();
  List<Character> stack6 = new LinkedList<>();
  List<Character> stack7 = new LinkedList<>();
  List<Character> stack8 = new LinkedList<>();
  List<Character> stack9 = new LinkedList<>();

  private void initQueues() {
    initQueue( 1, "HTZD" );
    initQueue( 2, "QRWTGCS" );
    initQueue( 3, "PBFQNRCH" );
    initQueue( 4, "LCNFHZ" );
    initQueue( 5, "GLFQS" );
    initQueue( 6, "VPWZBRCS" );
    initQueue( 7, "ZFJ" );
    initQueue( 8, "DLVZRHQ" );
    initQueue( 9, "BHGNFZLD" );
  }

  private void initQueue( int number, String s ) {
    for( char c : s.toCharArray() )
      getQueue( number ).add( c );
  }

  private List<Character> getQueue( int number ) {
    switch( number ) {
      case 1:
        return stack1;
      case 2:
        return stack2;
      case 3:
        return stack3;
      case 4:
        return stack4;
      case 5:
        return stack5;
      case 6:
        return stack6;
      case 7:
        return stack7;
      case 8:
        return stack8;
      case 9:
        return stack9;
      default:
        throw new IllegalArgumentException( "Error in stack number: " + number );
    }
  }

  @Test
  public void part1() throws IOException {

    String line;
    int move, from, to;
    initQueues();

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        if( !line.startsWith( "move" ) )
          continue;

        String[] values = line.split( " " );
        move = Integer.parseInt( values[1] );
        from = Integer.parseInt( values[3] );
        to = Integer.parseInt( values[5] );
        for( int i = 0; i < move; i++ )
          getQueue( to ).add( getQueue( from ).remove( getQueue( from ).size() - 1 ) );

      }

    }

    String result = "";
    for( int i = 1; i <= 9; i++ )
      result += getQueue( i ).get( getQueue( i ).size() - 1 );

    CP.print( "Day5, part1, crates on top", Map.of( "Result", result ) );

  }

  @Test
  public void part2() throws IOException {

    String line;
    int move, from, to;
    initQueues();

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        if( !line.startsWith( "move" ) )
          continue;

        String[] values = line.split( " " );
        move = Integer.parseInt( values[1] );
        from = Integer.parseInt( values[3] );
        to = Integer.parseInt( values[5] );
        for( int i = 0; i < move; i++ )
          getQueue( to ).add( getQueue( from ).remove( getQueue( from ).size() - ( move - i ) ) );

      }

    }

    String result = "";
    for( int i = 1; i <= 9; i++ )
      result += getQueue( i ).get( getQueue( i ).size() - 1 );

    CP.print( "Day5, part2, crates on top", Map.of( "Result", result ) );

  }

}
