package fun;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
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
public class Day05Test {

  private static final String FILE = "day05.txt";

  // Three commits exists for Day5, testing with different objects:
  // List, Stack, and Deque
  Deque<Character> stack1 = new LinkedList<>();
  Deque<Character> stack2 = new LinkedList<>();
  Deque<Character> stack3 = new LinkedList<>();
  Deque<Character> stack4 = new LinkedList<>();
  Deque<Character> stack5 = new LinkedList<>();
  Deque<Character> stack6 = new LinkedList<>();
  Deque<Character> stack7 = new LinkedList<>();
  Deque<Character> stack8 = new LinkedList<>();
  Deque<Character> stack9 = new LinkedList<>();

  private void initStacks() {
    initStack( 1, "HTZD" );
    initStack( 2, "QRWTGCS" );
    initStack( 3, "PBFQNRCH" );
    initStack( 4, "LCNFHZ" );
    initStack( 5, "GLFQS" );
    initStack( 6, "VPWZBRCS" );
    initStack( 7, "ZFJ" );
    initStack( 8, "DLVZRHQ" );
    initStack( 9, "BHGNFZLD" );
  }

  private void initStack( int number, String s ) {
    for( char c : s.toCharArray() )
      getStack( number ).add( c );
  }

  private Deque<Character> getStack( int number ) {
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
    initStacks();

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        if( !line.startsWith( "move" ) )
          continue;

        String[] values = line.split( " " );
        move = Integer.parseInt( values[1] );
        from = Integer.parseInt( values[3] );
        to = Integer.parseInt( values[5] );
        for( int i = 0; i < move; i++ )
          getStack( to ).add( getStack( from ).removeLast() );

      }

    }

    String result = "";
    for( int i = 1; i <= 9; i++ )
      result += getStack( i ).peekLast();

    CP.print( "Day5, part1, crates on top", Map.of( "Result", result ) );

  }

  @Test
  public void part2() throws IOException {

    String line;
    int move, from, to;
    initStacks();

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        if( !line.startsWith( "move" ) )
          continue;

        String[] values = line.split( " " );
        move = Integer.parseInt( values[1] );
        from = Integer.parseInt( values[3] );
        to = Integer.parseInt( values[5] );

        Deque<Character> tmpList = new LinkedList<>();
        for( int i = 0; i < move; i++ )
          tmpList.push( getStack( from ).removeLast() );
        getStack( to ).addAll( tmpList );

      }

    }

    String result = "";
    for( int i = 1; i <= 9; i++ )
      result += getStack( i ).peekLast();

    CP.print( "Day5, part2, crates on top", Map.of( "Result", result ) );

  }

}
