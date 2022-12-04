package fun;

import java.io.BufferedReader;
import java.io.IOException;
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
public class Day4Test {

  private static final String FILE = "day4.txt";

  @Test
  public void part1() throws IOException {

    String line;
    int number = 0;

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        String[] values = line.split( "," );
        String[] range1 = values[0].split( "-" );
        String[] range2 = values[1].split( "-" );
        int min1 = Integer.parseInt( range1[0] );
        int max1 = Integer.parseInt( range1[1] );
        int min2 = Integer.parseInt( range2[0] );
        int max2 = Integer.parseInt( range2[1] );
        if( ( min1 >= min2 && max1 <= max2 ) || ( min2 >= min1 && max2 <= max1 ) )
          number++;

      }

    }

    CP.print( "Day4, part1, assignment pairs inclusion", Map.of( "Number", number ) );

  }

  @Test
  public void part2() throws IOException {

    String line;
    int number = 0;

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        String[] values = line.split( "," );
        String[] range1 = values[0].split( "-" );
        String[] range2 = values[1].split( "-" );
        int min1 = Integer.parseInt( range1[0] );
        int max1 = Integer.parseInt( range1[1] );
        int min2 = Integer.parseInt( range2[0] );
        int max2 = Integer.parseInt( range2[1] );
        if( !( min1 > max2 || min2 > max1 ) )
          number++;

      }

    }

    CP.print( "Day4, part2, assignment pairs overlapping", Map.of( "Number", number ) );

  }

}
