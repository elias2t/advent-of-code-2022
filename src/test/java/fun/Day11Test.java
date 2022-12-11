package fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class Day11Test {

  private static final String FILE = "day11.txt";

  private List<Day11Test.Monkey> monkeys;

  @Getter
  private static class Monkey {

    private final int id;

    private final LinkedList<Integer> itemsWL;

    private final Function<Long, Long> inspectOperation;

    private BiFunction<Long, Long, Long> operate( String operator ) {
      return operator.equals( "*" ) ? ( a, b ) -> a * b : ( a, b ) -> a + b;
    }

    private final long divisible;

    private final Function<Long, Long> test;

    private int inspectTimes = 0;

    public Monkey( String[] lines ) {
      id = Integer.parseInt( lines[0].split( " " )[1].split( ":" )[0] );
      itemsWL = Arrays.stream( lines[1].substring( 18 ).split( ", " ) ).map( Integer::parseInt )
          .collect( Collectors.toCollection( LinkedList::new ) );
      String[] op = lines[2].substring( 19 ).split( " " );
      inspectOperation = old -> operate( op[1] )
          .apply( old, op[2].equals( "old" ) ? old : Long.parseLong( op[2] ) );
      divisible = Long.parseLong( lines[3].substring( 21 ) );
      long ifTrue = Long.parseLong( lines[4].substring( 29 ) );
      long ifFalse = Long.parseLong( lines[5].substring( 30 ) );
      test = v -> v % divisible == 0 ? ifTrue : ifFalse;
    }

    public void throwItems( List<Day11Test.Monkey> monkeys, Long part2 ) {
      while( !itemsWL.isEmpty() ) {
        inspectTimes++;
        long worryLevel = itemsWL.pollFirst();
        worryLevel = inspectOperation.apply( worryLevel );
        worryLevel = part2 != null ? worryLevel % part2 : worryLevel / 3;
        monkeys.get( test.apply( worryLevel ).intValue() ).addItemWL( (int) worryLevel );
      }
    }

    public void addItemWL( int itemWl ) {
      itemsWL.add( itemWl );
    }

  }

  private void readInput() throws IOException {

    Stream<String> stream = FileUtils.fromResourcesAsStream( FILE );
    monkeys = new ArrayList<>();
    int i = 0;
    String[] monkey = new String[7];
    Iterator<String> it = stream.iterator();
    while( it.hasNext() ) {
      monkey[i++] = it.next();
      if( i == 6 )
        monkeys.add( new Day11Test.Monkey( monkey ) );
      if( i == 7 )
        i = 0;
    }

  }

  @Test
  public void part1() throws IOException {

    readInput();
    int rounds = 20;

    for( int r = 0; r < rounds; r++ )
      for( int id = 0; id < monkeys.size(); id++ )
        monkeys.get( id ).throwItems( monkeys, null );

    List<Integer> inspectionsOrdered = monkeys.stream().map( Monkey::getInspectTimes )
        .sorted( Collections.reverseOrder() ).collect( Collectors.toList() );

    CP.print(
        "Day11, part1, the level of monkey business after 20 rounds",
        Map.of( "level", inspectionsOrdered.get( 0 ) * inspectionsOrdered.get( 1 ) ) );

  }

  @Test
  public void part2() throws IOException {

    readInput();
    int rounds = 10000;
    long part2 = monkeys.stream().map( Monkey::getDivisible ).reduce( 1L, ( a, b ) -> a * b );

    for( int r = 0; r < rounds; r++ )
      for( int id = 0; id < monkeys.size(); id++ )
        monkeys.get( id ).throwItems( monkeys, part2 );

    List<Integer> inspectionsOrdered = monkeys.stream().map( Monkey::getInspectTimes )
        .sorted( Collections.reverseOrder() ).collect( Collectors.toList() );

    CP.print(
        "Day11, part2, the level of monkey business after 10000 rounds without dividing by 3",
        Map.of( "level", (long) inspectionsOrdered.get( 0 ) * inspectionsOrdered.get( 1 ) ) );

  }

}
