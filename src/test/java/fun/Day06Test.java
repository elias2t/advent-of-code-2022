package fun;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.CharUtils;
import fun.utils.FileUtils;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day06Test {

  private static final String FILE = "day06.txt";

  private boolean areDifferent( char a, char b, char c, char d ) {
    return a != b && a != c && a != d && b != c && b != d && c != d;
  }

  private boolean areDifferent( char ... chars ) {
    return CharUtils.asSet( chars ).size() == chars.length;
  }

  @Test
  public void part1() throws IOException {

    String line;
    List<Integer> resultPerLine = new ArrayList<>();

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        char[] chars = line.toCharArray();
        for( int i = 0; i < chars.length; i++ )
          if( areDifferent( chars[i], chars[i + 1], chars[i + 2], chars[i + 3] ) ) {
            resultPerLine.add( i + 4 );
            break;
          }

      }

    }

    CP.print(
        "Day6, part1, characters to be processed before start-of-packet detection",
        Map.of( "Result", resultPerLine ) );

  }

  @Test
  public void part2() throws IOException {

    String line;
    List<Integer> resultPerLine = new ArrayList<>();

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        char[] chars = line.toCharArray();
        for( int i = 0; i < chars.length; i++ )
          if( areDifferent( Arrays.copyOfRange( chars, i, i + 14 ) ) ) {
            resultPerLine.add( i + 14 );
            break;
          }

      }

    }

    CP.print(
        "Day6, part2, characters to be processed before start-of-message detection",
        Map.of( "Result", resultPerLine ) );

  }

}
