package fun;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class Day03Test {

  private static final String FILE = "day03.txt";

  private int getPower( char cr ) {
    if( Character.isLowerCase( cr ) )
      return cr - 'a' + 1;
    else
      return cr - 'A' + 27;
  }

  @Test
  public void part1() throws IOException {

    String line;
    int sum = 0;

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        char[] chars = line.toCharArray();
        List<Character> part = new ArrayList<>();
        for( int i = 0; i < chars.length / 2; i++ )
          part.add( chars[i] );
        for( int i = chars.length / 2; i < chars.length; i++ )
          if( part.contains( chars[i] ) ) {
            sum += getPower( chars[i] );
            break;
          }

      }

    }

    CP.print( "Day3, part1, sum of priorities", Map.of( "Sum", sum ) );

  }

  @Test
  public void part2() throws IOException {

    String line;
    int sum = 0;
    List<Character> line1 = null;
    List<Character> line2 = null;

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        if( line1 == null )
          line1 = line.chars().mapToObj( c -> (char) c ).collect( Collectors.toList() );
        else if( line2 == null )
          line2 = line.chars().mapToObj( c -> (char) c ).collect( Collectors.toList() );
        else {
          char[] chars = line.toCharArray();
          for( char aChar : chars )
            if( line1.contains( aChar ) && line2.contains( aChar ) ) {
              sum += getPower( aChar );
              break;
            }
          line1 = null;
          line2 = null;
        }

      }

    }

    CP.print( "Day3, part2, sum of priorities", Map.of( "Sum", sum ) );

  }

}
