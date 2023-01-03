package fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
public class Day13Test {

  private static final String FILE = "day13.txt";

  public static final char LB = '[';
  public static final char RB = ']';
  public static final char COMMA = ',';

  private final LinkedList<Pair> pairs = new LinkedList<>( List.of( new Pair() ) );

  public static class Pair {

    public Object leftO;

    public Object rightO;

    public void set( String s ) {
      if( leftO == null )
        leftO = stringToO( s );
      else
        rightO = stringToO( s );
    }

    public boolean isCorrect() {
      return is1SmallerOrEqual( leftO, rightO ) >= 0;
    }

  }

  private void readInput() throws IOException {
    FileUtils.fromResourcesAsList( FILE ).forEach( l -> {
      if( l.isBlank() )
        pairs.addLast( new Pair() );
      else
        pairs.getLast().set( l );
    } );
  }

  private static int is1SmallerOrEqual( Object item1, Object item2 ) {
    if( item1 instanceof Integer && item2 instanceof Integer )
      return (Integer) item2 - (Integer) item1;
    else if( item1 instanceof Integer )
      return is1SmallerOrEqual( List.of( item1 ), item2 );
    else if( item2 instanceof Integer )
      return is1SmallerOrEqual( item1, List.of( item2 ) );
    else {
      List<?> list1 = (List<?>) item1;
      List<?> list2 = (List<?>) item2;
      for( int i = 0; i < Math.min( list1.size(), list2.size() ); i++ ) {
        int r = is1SmallerOrEqual( list1.get( i ), list2.get( i ) );
        if( r != 0 )
          return r;
      }
      return list2.size() - list1.size();
    }
  }

  private static Object stringToO( String section ) {
    if( section.length() == 2 && section.indexOf( LB ) == 0 )
      return List.of();
    if( section.charAt( 0 ) != LB && !section.contains( "," ) )
      return Integer.parseInt( section );

    List<Object> list = new ArrayList<>();
    int LRBs = 0;
    int lastComma = 0;
    for( int i = 1; i < section.length(); i++ ) {
      char c = section.charAt( i );
      LRBs += c == LB ? 1 : c == RB ? -1 : 0;
      if( ( c == COMMA && LRBs == 0 ) || i == section.length() - 1 ) {
        list.add( stringToO( section.substring( lastComma + 1, i ) ) );
        lastComma = i;
      }
    }
    return list;
  }

  @Test
  public void part1() throws IOException {
    readInput();
    CP.print(
        "Day13, part1, sum of the indices pairs with the right order",
        Map.of(
            "sum",
            pairs.stream().mapToInt( p -> p.isCorrect() ? pairs.indexOf( p ) + 1 : 0 ).sum() ) );
  }

  @Test
  public void part2() throws IOException {
    readInput();
    Object o1 = stringToO( "[[2]]" );
    Object o2 = stringToO( "[[6]]" );
    List<Object> asList = new ArrayList<>( List.of( o1, o2 ) );
    pairs.forEach( p -> asList.addAll( List.of( p.leftO, p.rightO ) ) );
    asList.sort( Day13Test::is1SmallerOrEqual );
    Collections.reverse( asList ); // because my comparator is reversed
    CP.print(
        "Day13, part2, the decoder key for the distress signal",
        Map.of( "key", ( asList.indexOf( o1 ) + 1 ) * ( asList.indexOf( o2 ) + 1 ) ) );
  }

}
