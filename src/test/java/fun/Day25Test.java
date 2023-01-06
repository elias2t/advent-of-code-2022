package fun;

import java.io.IOException;
import java.util.ArrayList;
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
public class Day25Test {

  private static final String FILE = "day25.txt";

  private final List<Long> decimals = new ArrayList<>();

  private void readInput() throws IOException {
    List<String> snafus = FileUtils.fromResourcesAsList( FILE );
    snafus.forEach( s -> decimals.add( toDecimal( s ) ) );
  }

  public long toDecimal( String snafu ) {
    long result = 0;
    for( int i = 0; i < snafu.length(); i++ ) {
      char c = snafu.charAt( snafu.length() - i - 1 );
      long m = (long) Math.pow( 5, i );
      result += c == '-' ? -1 * m : c == '=' ? -2 * m : ( c - '0' ) * m;
    }
    return result;
  }

  public String toSnafu( long decimal ) {
    // '=' => 5 - 2
    // '-' => 5 - 1
    String result = "";
    while( decimal > 0 ) {
      long r = decimal % 5;
      decimal /= 5;
      if( r > 2 )
        decimal++;
      result += r == 3 ? "=" : r == 4 ? "-" : r;
    }
    return new StringBuilder( result ).reverse().toString();
  }

  @Test
  public void part1() throws IOException {
    readInput();
    CP.print(
        "Day25, part1, SNAFU number to supply to Bob's console",
        Map.of( "number", toSnafu( decimals.stream().mapToLong( v -> v ).sum() ) ) );
  }

  @Test
  public void part2() throws IOException {
    CP.print( "Day25, part2, stars", Map.of( "stars", 50 ) );
  }

}
