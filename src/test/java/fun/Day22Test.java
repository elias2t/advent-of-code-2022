package fun;

import java.io.IOException;
import java.util.ArrayList;
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
public class Day22Test {

  private static final String FILE = "day22.txt";

  private final LinkedList<String> moves = new LinkedList<>();
  private int[][] m;

  private final List<Integer> limitsLeftByRow = new ArrayList<>();
  private final List<Integer> limitsRightByRow = new ArrayList<>();
  private final List<Integer> limitsUpByColumn = new ArrayList<>();
  private final List<Integer> limitsDownByColumn = new ArrayList<>();

  // Current live changing values for position
  private int x = 0;
  private int y = 0;
  private int facingHour = 3;
  private boolean part1 = true;

  private void readInput() throws IOException {

    List<String> lines = FileUtils.fromResourcesAsList( FILE );
    String lastLine = lines.remove( lines.size() - 1 ); // Moves line
    lines.remove( lines.size() - 1 ); // Empty line

    // Moves
    String[] byR = lastLine.split( "R" );
    for( String s : byR ) {
      String[] byL = s.split( "L" );
      for( int i = 0; i < byL.length; i++ ) {
        moves.add( byL[i] );
        if( byL.length > 1 && i != byL.length - 1 )
          moves.add( "L" );
      }
      moves.add( "R" );
    }
    moves.removeLast();

    // Matrix
    int maxRow = lines.size();
    int maxColumn = lines.stream().mapToInt( String::length ).max().orElse( 0 );
    m = new int[maxRow][maxColumn];
    for( int i = 0; i < maxRow; i++ ) {
      String l = lines.get( i );
      for( int j = 0; j < maxColumn; j++ )
        if( j < l.length() ) {
          char c = l.charAt( j );
          m[i][j] = c == '.' ? 1 : c == '#' ? 2 : 0;
        }
    }

    // Limits left/right
    for( int i = 0; i < maxRow; i++ ) {
      int left = -1;
      int right = -1;
      for( int j = 0; j < maxColumn; j++ )
        if( m[i][j] > 0 ) {
          if( left == -1 )
            left = j;
          else
            right = j;
        }
      limitsLeftByRow.add( left );
      limitsRightByRow.add( right );
    }

    // Limits up/down
    for( int j = 0; j < maxColumn; j++ ) {
      int up = -1;
      int down = -1;
      for( int i = 0; i < maxRow; i++ )
        if( m[i][j] > 0 ) {
          if( up == -1 )
            up = i;
          else
            down = i;
        }
      limitsUpByColumn.add( up );
      limitsDownByColumn.add( down );
    }

    // First position
    for( int j = 0; j < maxColumn; j++ )
      if( m[0][j] == 1 ) {
        x = j;
        break;
      }

  }

  private void run() {
    for( String s : moves )
      switch( s ) {
        case "R":
          facingHour = ( facingHour + 3 ) % 12;
          break;
        case "L":
          facingHour = ( ( facingHour == 0 ? 12 : facingHour ) - 3 ) % 12;
          break;
        default:
          turnAndMove( Integer.parseInt( s ) );
      }
  }

  private void turnAndMove( int v ) {

    if( facingHour == 0 )
      for( int k = 0; k < v; k++ ) {
        int lU = limitsUpByColumn.get( x );
        int lD = limitsDownByColumn.get( x );
        int move = y - 1;
        if( move >= lU && m[move][x] == 1 )
          y = move;
        else if( part1 && move < lU && m[lD][x] == 1 )
          y = lD;
        else {
          if( !part1 && move < lU && canProject() )
            turnAndMove( v - k - 1 );
          break;
        }
      }
    else if( facingHour == 3 )
      for( int k = 0; k < v; k++ ) {
        int lL = limitsLeftByRow.get( y );
        int lR = limitsRightByRow.get( y );
        int move = x + 1;
        if( move <= lR && m[y][move] == 1 )
          x = move;
        else if( part1 && move > lR && m[y][lL] == 1 )
          x = lL;
        else {
          if( !part1 && move > lR && canProject() )
            turnAndMove( v - k - 1 );
          break;
        }
      }
    else if( facingHour == 6 )
      for( int k = 0; k < v; k++ ) {
        int lU = limitsUpByColumn.get( x );
        int lD = limitsDownByColumn.get( x );
        int move = y + 1;
        if( move <= lD && m[move][x] == 1 )
          y = move;
        else if( part1 && move > lD && m[lU][x] == 1 )
          y = lU;
        else {
          if( !part1 && move > lD && canProject() )
            turnAndMove( v - k - 1 );
          break;
        }
      }
    else if( facingHour == 9 )
      for( int k = 0; k < v; k++ ) {
        int lL = limitsLeftByRow.get( y );
        int lR = limitsRightByRow.get( y );
        int move = x - 1;
        if( move >= lL && m[y][move] == 1 )
          x = move;
        else if( part1 && move < lL && m[y][lR] == 1 )
          x = lR;
        else {
          if( !part1 && move < lL && canProject() )
            turnAndMove( v - k - 1 );
          break;
        }
      }

  }

  public int getFacingResult() {
    return facingHour == 0 ? 3 : ( facingHour - 3 ) / 3;
  }

  @Test
  public void part1() throws IOException {
    readInput();
    run();
    CP.print(
        "Day22, part1, given the monkeys notes, the password is",
        Map.of( "password", 1000 * ( y + 1 ) + 4 * ( x + 1 ) + getFacingResult() ) );
  }

  private boolean canProject() {

    int CSS = 50; // Cube side size

    if( x == 0
        && y >= ( CSS * 2 )
        && y < ( CSS * 3 )
        && m[CSS - 1 - ( y - 2 * CSS )][CSS] == 1
        && facingHour == 9 ) { // 3DownToUp
      x = CSS;
      y = CSS - 1 - ( y - 2 * CSS );
      facingHour = 3;
      return true;
    } else if( x == CSS && y < CSS && m[( CSS - y - 1 ) + 2 * CSS][0] == 1 && facingHour == 9 ) { // 3upToDown
      x = 0;
      y = ( CSS - y - 1 ) + 2 * CSS;
      facingHour = 3;
      return true;
    } else if( x < CSS && y == 2 * CSS && m[x + CSS][CSS] == 1 && facingHour == 0 ) { // 2DownToUp
      y = x + CSS;
      x = CSS;
      facingHour = 3;
      return true;
    } else if( x == CSS
        && y < CSS * 2
        && y >= CSS
        && m[2 * CSS][y - CSS] == 1
        && facingHour == 9 ) { // 2upToDown
      x = y - CSS;
      y = 2 * CSS;
      facingHour = 6;
      return true;
    } else if( x == 0 && y >= CSS * 3 && m[0][y - CSS * 3 + CSS] == 1 && facingHour == 9 ) { // 4DownToUp
      x = y - CSS * 3 + CSS;
      y = 0;
      facingHour = 6;
      return true;
    } else if( x >= CSS
        && x < ( CSS * 2 )
        && y == 0
        && m[CSS * 2 + x][0] == 1
        && facingHour == 0 ) { // 4upToDown
      y = CSS * 2 + x;
      x = 0;
      facingHour = 3;
      return true;
    } else if( x < CSS && y == CSS * 4 - 1 && m[0][x + 2 * CSS] == 1 && facingHour == 6 ) { // 7DownToUp
      x = x + 2 * CSS;
      y = 0;
      return true;
    } else if( x >= CSS * 2 && y == 0 && m[CSS * 4 - 1][x - CSS * 2] == 1 && facingHour == 0 ) { // 7upToDown
      x = x - CSS * 2;
      y = CSS * 4 - 1;
      return true;
    } else if( x == CSS - 1
        && y >= CSS * 3
        && m[3 * CSS - 1][y - CSS * 3 + CSS] == 1
        && facingHour == 3 ) { // iDownToUp
      x = y - CSS * 3 + CSS;
      y = 3 * CSS - 1;
      facingHour = 0;
      return true;
    } else if( x >= CSS
        && x < CSS * 2
        && y == CSS * 3 - 1
        && m[3 * CSS + x - CSS][CSS - 1] == 1
        && facingHour == 6 ) { // 1upToDown
      y = 3 * CSS + x - CSS;
      x = CSS - 1;
      facingHour = 9;
      return true;
    } else if( x == 2 * CSS - 1
        && y < CSS * 2
        && y >= CSS
        && m[CSS - 1][y - CSS + 2 * CSS] == 1
        && facingHour == 3 ) {// 6DownToUp
      x = y - CSS + 2 * CSS;
      y = CSS - 1;
      facingHour = 0;
      return true;
    } else if( x >= 2 * CSS
        && x < 3 * CSS
        && y == CSS - 1
        && m[x - 2 * CSS + CSS][2 * CSS - 1] == 1
        && facingHour == 6 ) {// 6upToDown
      y = x - 2 * CSS + CSS;
      x = 2 * CSS - 1;
      facingHour = 9;
      return true;
    } else if( x == 2 * CSS - 1
        && y < 3 * CSS
        && y >= 2 * CSS
        && m[CSS - 1 - ( y - 2 * CSS )][3 * CSS - 1] == 1
        && facingHour == 3 ) {// 5DownToUp
      x = 3 * CSS - 1;
      y = CSS - 1 - ( y - 2 * CSS );
      facingHour = 9;
      return true;
    } else if( x == 3 * CSS - 1
        && y < CSS
        && m[3 * CSS - y - 1][2 * CSS - 1] == 1
        && facingHour == 3 ) {// 5upToDown
      x = 2 * CSS - 1;
      y = 3 * CSS - y - 1;
      facingHour = 9;
      return true;
    } else
      return false;

  }

  @Test
  public void part2() throws IOException {
    readInput();
    part1 = false;
    run();
    CP.print(
        "Day22, part2, given the monkeys notes, the password is",
        Map.of( "password", 1000 * ( y + 1 ) + 4 * ( x + 1 ) + getFacingResult() ) );
  }

}
