package fun;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.FileUtils;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
public class Day1Test {

  private static final String FILE = "day1.txt";

  @Test
  public void part1() throws IOException {

    String line;
    int count = 1;
    int elf = 1;
    int sum = 0;
    int max = 0;

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        if( !line.isBlank() ) {
          sum += Integer.parseInt( line );
          if( sum > max ) {
            max = sum;
            elf = count;
          }
        } else {
          count++;
          sum = 0;
        }

      }

    }

    CP.print( "Elf carrying the most Calories", Map.of( "Elf number", elf, "Calories", max ) );

  }

  @Test
  public void part2() throws IOException {

    String line;
    int sum = 0;
    List<Integer> list = new ArrayList<>();

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        if( !line.isBlank() ) {
          sum += Integer.parseInt( line );
        } else {
          list.add( sum );
          sum = 0;
        }

      }
      list.add( sum );

    }
    Collections.sort( list, Collections.reverseOrder() );

    CP.print(
        "Three Elves carrying the most Calories",
        Map.of( "Calories", list.get( 0 ) + list.get( 1 ) + list.get( 2 ) ) );

  }

}
