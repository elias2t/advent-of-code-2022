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
public class Day02Test {

  private static final String FILE = "day02.txt";

  @Test
  public void part1() throws IOException {

    String line;
    int score = 0;

    final int rock = 1; // A & X
    final int paper = 2; // B & Y
    final int scissors = 3; // C & Z

    final int lost = 0;
    final int draw = 3;
    final int won = 6;

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        String[] values = line.split( " " );
        switch( values[0] ) {

          case "A":
            score += values[1].equals( "X" ) ? draw + rock
                : values[1].equals( "Y" ) ? won + paper : lost + scissors;
            break;
          case "B":
            score += values[1].equals( "X" ) ? lost + rock
                : values[1].equals( "Y" ) ? draw + paper : won + scissors;
            break;
          case "C":
            score += values[1].equals( "X" ) ? won + rock
                : values[1].equals( "Y" ) ? lost + paper : draw + scissors;
            break;

        }

      }

    }

    CP.print(
        "Day2, part1, total score if everything goes exactly according to strategy guide",
        Map.of( "Score", score ) );

  }

  @Test
  public void part2() throws IOException {

    String line;
    int score = 0;

    final int rock = 1; // A
    final int paper = 2; // B
    final int scissors = 3; // C

    final int lost = 0; // X
    final int draw = 3; // Y
    final int won = 6; // Z

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        String[] values = line.split( " " );
        switch( values[0] ) {

          case "A":
            score += values[1].equals( "X" ) ? lost + scissors
                : values[1].equals( "Y" ) ? draw + rock : won + paper;
            break;
          case "B":
            score += values[1].equals( "X" ) ? lost + rock
                : values[1].equals( "Y" ) ? draw + paper : won + scissors;
            break;
          case "C":
            score += values[1].equals( "X" ) ? lost + paper
                : values[1].equals( "Y" ) ? draw + scissors : won + rock;
            break;

        }

      }

    }

    CP.print(
        "Day2, part2, total score if everything goes exactly according to strategy guide",
        Map.of( "Score", score ) );

  }

}
