package fun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class Day21Test {

  private static final String FILE = "day21.txt";

  private final Map<String, Monkey> monkeys = new HashMap<>();

  private void readInput() throws IOException {
    Iterator<String> it = FileUtils.fromResourcesAsStream( FILE ).iterator();
    while( it.hasNext() ) {
      String[] data = it.next().split( ": " );
      monkeys.put( data[0], new Monkey( data[0], data[1].split( " " ) ) );
    }
  }

  @Data
  public static class Monkey {

    public String id;

    public long number;

    public boolean isOP;

    public String operation;

    public String subId1;

    public String subId2;

    public Monkey sub1;

    public Monkey sub2;

    public Long result;

    public Monkey( String id, String[] op ) {
      this.id = id;
      if( op.length == 1 ) {
        number = Long.parseLong( op[0] );
      } else {
        isOP = true;
        subId1 = op[0];
        operation = op[1];
        subId2 = op[2];
      }
    }

    public void setSubObjects( Map<String, Monkey> monkeys ) {
      this.sub1 = monkeys.get( subId1 );
      this.sub2 = monkeys.get( subId2 );
    }

    public long getResult() {
      if( result == null )
        result = isOP ? operate() : number;
      return result;
    }

    public long operate() {
      switch( operation ) {
        case "*":
          return sub1.getResult() * sub2.getResult();
        case "-":
          return sub1.getResult() - sub2.getResult();
        case "+":
          return sub1.getResult() + sub2.getResult();
        case "/":
          return sub1.getResult() / sub2.getResult();
        default:
          throw new RuntimeException( operation );
      }
    }

    public boolean containsHumn() {
      return isOP
          && ( subId2.equals( "humn" )
              || subId1.equals( "humn" )
              || id.equals( "humn" )
              || sub1.containsHumn()
              || sub2.containsHumn() );
    }

    public void fromResult( long result ) {

      boolean isToFindLeft = sub1.containsHumn();
      Monkey known = isToFindLeft ? sub2 : sub1;
      Monkey toFind = isToFindLeft ? sub1 : sub2;

      if( id.equals( "root" ) )
        toFind.fromResult( result );
      else if( toFind.isOP )
        toFind.fromResult( reverse( known.getResult(), result, isToFindLeft ) );
      else
        toFind.number = reverse( known.getResult(), result, isToFindLeft );

    }

    public long reverse( long value, long result, boolean isToFindLeft ) {
      switch( operation ) {
        case "*":
          return result / value;
        case "-":
          return isToFindLeft ? result + value : value - result;
        case "+":
          return result - value;
        case "/":
          return isToFindLeft ? result * value : value / result;
        default:
          throw new RuntimeException( operation );
      }
    }

  }

  public long treat( boolean part2 ) {

    monkeys.values().forEach( m -> m.setSubObjects( monkeys ) );
    Monkey root = monkeys.get( "root" );

    if( !part2 )
      return root.getResult();
    else {
      Monkey known = root.sub1.containsHumn() ? root.sub2 : root.sub1;
      root.fromResult( known.getResult() );
      return monkeys.get( "humn" ).number;
    }

  }

  @Test
  public void part1() throws IOException {
    readInput();
    CP.print(
        "Day21, part1, number will the monkey named root yell",
        Map.of( "number", treat( false ) ) );
  }

  @Test
  public void part2() throws IOException {
    readInput();
    CP.print( "Day21, part2, number to yell at start", Map.of( "number", treat( true ) ) );
  }

}
