package fun;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
public class Day10Test {

  private static final String FILE = "day10.txt";

  private static final Set<Integer> STRENGTH_CYCLES = Set.of( 20, 60, 100, 140, 180, 220 );

  private static class Holder {

    private Integer cycle = 1;

    private Integer X = 1;

    @Getter
    private Integer strength = 0;

    @Getter
    private String screen = "\n#"; // first print always # cause X = 1;

    private void increment() {
      cycle++;
      if( STRENGTH_CYCLES.contains( cycle ) )
        strength += X * cycle;
      if( cycle <= 240 )
        print();
    }

    private void print() {
      int row = ( cycle - 1 ) / 40;
      int rowPosition = cycle - 1 - row * 40;
      int spriteMidPosition = X;
      screen += Math.abs( spriteMidPosition - rowPosition ) <= 1 ? '#' : ".";
      screen += cycle % 40 == 0 ? '\n' : "";
    }

    public void move( Line l ) {
      increment();
      if( !l.isNoop ) {
        X += l.value;
        increment();
      }
    }

  }

  private static class Line {

    private final boolean isNoop;

    private final int value;

    public Line( String line ) {
      String[] s = line.split( " " );
      isNoop = s[0].equals( "noop" );
      value = s.length == 2 ? Integer.parseInt( s[1] ) : 0;
    }

  }

  @Test
  public void part1() throws IOException {

    Holder h = new Holder();
    FileUtils.fromResourcesAsStream( FILE ).map( Line::new ).forEach( h::move );

    CP.print(
        "Day10, part1, the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles",
        Map.of( "strength", h.getStrength() ) );

  }

  @Test
  public void part2() throws IOException {

    Holder h = new Holder();
    FileUtils.fromResourcesAsStream( FILE ).map( Line::new ).forEach( h::move );

    CP.print( "Day10, part2, the image given by the program", Map.of( "screen", h.getScreen() ) );

  }

}
