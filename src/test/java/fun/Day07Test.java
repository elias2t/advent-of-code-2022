package fun;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
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
import lombok.Data;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day07Test {

  private static final String FILE = "day07.txt";

  @Data
  private static class Dir {

    private String id = "";

    private final LinkedList<String> path;

    private final List<String> subDirs = new ArrayList<>();

    private int size = 0;

    public Dir( LinkedList<String> path ) {
      this.path = path;
      for( String s : path )
        this.id += s;
    }

    public void addSub( String sub ) {
      subDirs.add( id + sub );
    }

    public void addSize( int subSize ) {
      size += subSize;
    }

  }

  public LinkedHashMap<String, Dir> part1() throws IOException {

    int result = 0;
    String line, currentDirAlias;
    LinkedList<String> currentPath = new LinkedList<>();
    Dir localDir = new Dir( currentPath );
    LinkedHashMap<String, Dir> dirs = new LinkedHashMap<>();

    try( BufferedReader br = new BufferedReader( FileUtils.fromResources( FILE ) ) ) {

      while( ( line = br.readLine() ) != null ) {

        if( line.startsWith( "$ cd" ) ) {
          currentDirAlias = line.substring( 5 );
          if( currentDirAlias.equals( ".." ) )
            currentPath.removeLast();
          else {
            currentPath.addLast( currentDirAlias );
            localDir = new Dir( currentPath );
            dirs.put( localDir.getId(), localDir );
          }
        } else if( line.startsWith( "dir" ) )
          localDir.addSub( line.split( " " )[1] );
        else if( !line.startsWith( "$ ls" ) )
          localDir.addSize( Integer.parseInt( line.split( " " )[0] ) );

      }

    }

    List<Dir> reversedList = new ArrayList<>( dirs.values() );
    Collections.reverse( reversedList );
    for( Dir dir : reversedList ) {
      for( String subDir : dir.getSubDirs() )
        dir.addSize( dirs.get( subDir ).getSize() );
      if( dir.getSize() <= 100000 )
        result += dir.getSize();
    }

    CP.print(
        "Day7, part1, sum of directories with size at most 100000",
        Map.of( "Result", result ) );

    return dirs;

  }

  @Test
  public void part2() throws IOException {

    LinkedHashMap<String, Dir> dirs = part1();
    int totalUsed = dirs.get( "/" ).getSize();
    int needed = 30000000 - ( 70000000 - totalUsed );
    int found =
        dirs.values().stream().mapToInt( Dir::getSize ).filter( a -> a > needed ).min().orElse( 0 );

    CP.print(
        "Day7, part2, smallest directory to delete",
        Map.of(
            "Total used space",
            totalUsed,
            "Needed space",
            needed,
            "Minimum to delete",
            found ) );

  }

}
