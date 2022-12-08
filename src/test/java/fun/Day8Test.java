package fun;

import java.io.IOException;
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
public class Day8Test {

  private static final String FILE = "day8.txt";

  @Test
  public void part1() throws IOException {

    int result = 0;

    List<String> lines = FileUtils.fromResourcesAsList( FILE );
    int wide = lines.get( 0 ).length(); // i
    int height = lines.size(); // j
    int[][] m = new int[height][wide];

    // Fill the matrix
    for( int j = 0; j < height; j++ ) {
      String line = lines.get( j );
      for( int i = 0; i < wide; i++ )
        m[j][i] = Integer.parseInt( line.charAt( i ) + "" );
    }

    // Edge trees
    result += height * 2 + ( wide - 2 ) * 2;

    // Process inner trees
    for( int i = 1; i < wide - 1; i++ )
      for( int j = 1; j < height - 1; j++ ) {
        boolean isVisibleRowLeft = true;
        boolean isVisibleRowRight = true;
        boolean isVisibleColDown = true;
        boolean isVisibleColUp = true;
        int item = m[j][i];
        for( int k = 0; k < wide; k++ ) {
          if( k < i && m[j][k] >= item )
            isVisibleRowLeft = false;
          if( k > i && m[j][k] >= item )
            isVisibleRowRight = false;
        }
        for( int k = 0; k < height; k++ ) {
          if( k < j && m[k][i] >= item )
            isVisibleColDown = false;
          if( k > j && m[k][i] >= item )
            isVisibleColUp = false;
        }
        if( isVisibleRowLeft || isVisibleRowRight || isVisibleColDown || isVisibleColUp )
          result++;
      }

    CP.print( "Day8, part1, number of visible trees", Map.of( "Result", result ) );

  }

  @Test
  public void part2() throws IOException {

    int result = 0;

    List<String> lines = FileUtils.fromResourcesAsList( FILE );
    int wide = lines.get( 0 ).length(); // i
    int height = lines.size(); // j
    int[][] m = new int[height][wide];

    // Fill the matrix
    for( int j = 0; j < height; j++ ) {
      String line = lines.get( j );
      for( int i = 0; i < wide; i++ )
        m[j][i] = Integer.parseInt( line.charAt( i ) + "" );
    }

    // Process all trees
    for( int i = 0; i < wide; i++ )
      for( int j = 0; j < height; j++ ) {
        int scoreRowLeft = 0;
        int scoreRowRight = 0;
        int scoreColDown = 0;
        int scoreColUp = 0;
        int item = m[j][i];
        for( int k = j - 1; k >= 0; k-- )
          if( m[k][i] < item )
            scoreRowLeft++;
          else {
            scoreRowLeft++;
            break;
          }
        for( int k = j + 1; k < wide; k++ )
          if( m[k][i] < item )
            scoreRowRight++;
          else {
            scoreRowRight++;
            break;
          }
        for( int k = i - 1; k >= 0; k-- )
          if( m[j][k] < item )
            scoreColUp++;
          else {
            scoreColUp++;
            break;
          }
        for( int k = i + 1; k < height; k++ )
          if( m[j][k] < item )
            scoreColDown++;
          else {
            scoreColDown++;
            break;
          }
        int localResult = scoreRowLeft * scoreRowRight * scoreColUp * scoreColDown;
        if( localResult > result )
          result = localResult;
      }

    CP.print( "Day8, part2, highest scenic score", Map.of( "Result", result ) );

  }

}
