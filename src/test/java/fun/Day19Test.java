package fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
import lombok.Data;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day19Test {

  private static final String FILE = "day19.txt";

  private final Map<Integer, Blueprint> blueprints = new HashMap<>();

  private List<Blueprint> nodes;

  public int minutes;

  private void readInput() throws IOException {

    Iterator<String> it = FileUtils.fromResourcesAsStream( FILE ).iterator();
    while( it.hasNext() ) {
      String[] data = it.next().replace( "Blueprint ", "" ).replace( " Each ore robot costs ", "" )
          .replace( " ore. Each clay robot costs ", "," )
          .replace( " ore. Each obsidian robot costs ", "," ).replace( " ore and ", "," )
          .replace( " clay. Each geode robot costs ", "," ).replace( " obsidian.", "" )
          .split( ":" );
      int id = Integer.parseInt( data[0] );
      blueprints.put(
          id,
          new Blueprint(
              id,
              Arrays.stream( data[1].split( "," ) ).map( Integer::parseInt )
                  .collect( Collectors.toList() ) ) );
    }

  }

  @Data
  public static class Blueprint {

    private final int id;

    private final List<Integer> orderedCosts;

    public final int maxOre;

    public Blueprint( int id, List<Integer> orderedCosts ) {
      this.id = id;
      this.orderedCosts = orderedCosts;
      this.maxOre = Math.max(
          orderedCosts.get( 4 ),
          Math.max(
              orderedCosts.get( 2 ),
              Math.max( orderedCosts.get( 1 ), orderedCosts.get( 0 ) ) ) );
    }

    // Robots
    public int oreR = 1;
    public int clayR = 0;
    public int obsidianR = 0;
    public int geodeR = 0;

    // Collects
    public int oreC = 0;
    public int clayC = 0;
    public int obsidianC = 0;
    public int geodeC = 0;

    public int getQuality() {
      return id * geodeC;
    }

    public static Blueprint newNode( Blueprint b, List<Blueprint> nodes ) {
      Blueprint n = new Blueprint( b.getId(), b.getOrderedCosts() );
      n.oreR = b.oreR;
      n.clayR = b.clayR;
      n.obsidianR = b.obsidianR;
      n.geodeR = b.geodeR;
      n.oreC = b.oreC;
      n.clayC = b.clayC;
      n.obsidianC = b.obsidianC;
      n.geodeC = b.geodeC;
      if( nodes != null )
        nodes.add( n );
      return n;
    }

  }

  private void doAction( Blueprint b, int maxMinutes ) {

    boolean createNewNode = false;
    Blueprint orig = Blueprint.newNode( b, null );

    /* Construct Geode */
    if( orig.oreC >= orig.getOrderedCosts().get( 4 )
        && orig.obsidianC >= orig.getOrderedCosts().get( 5 )
        && minutes <= maxMinutes - 1 ) { // -1 to use the robot once minimum
      b.oreC -= b.getOrderedCosts().get( 4 );
      b.obsidianC -= b.getOrderedCosts().get( 5 );
      collect( b );
      b.geodeR++;
      return; // createNewNode = true; not needed, big values for obsidian
    }
    /* Construct Obsidian */
    if( orig.oreC >= orig.getOrderedCosts().get( 2 )
        && orig.clayC >= orig.getOrderedCosts().get( 3 )
        && orig.obsidianR < orig.getOrderedCosts().get( 5 )
        && minutes <= maxMinutes - 1 - 3 ) { // -3 to use once by upper geode
      // if( createNewNode ) ... if above != return;
      b.oreC -= b.getOrderedCosts().get( 2 );
      b.clayC -= b.getOrderedCosts().get( 3 );
      collect( b );
      b.obsidianR++;
      return; // createNewNode = true; not needed, big values for clay
    }
    /* Construct Clay */
    if( orig.oreC >= orig.getOrderedCosts().get( 1 )
        && orig.clayR < orig.getOrderedCosts().get( 3 )
        && minutes <= maxMinutes - 1 - 3 - 3 ) { // upper obsidian and geode
      // if( createNewNode ) ... if above != return;
      b.oreC -= b.getOrderedCosts().get( 1 );
      collect( b );
      b.clayR++;
      createNewNode = true;
    }
    /* Construct Ore */
    if( orig.oreC >= orig.getOrderedCosts().get( 0 )
        && orig.oreR < orig.maxOre
        && minutes <= maxMinutes - 1 - 3 ) { // upper once any level
      if( createNewNode ) {
        Blueprint n = Blueprint.newNode( orig, nodes );
        n.oreC -= n.getOrderedCosts().get( 0 );
        collect( n );
        n.oreR++;
      } else {
        b.oreC -= b.getOrderedCosts().get( 0 );
        collect( b );
        b.oreR++;
        createNewNode = true;
      }
    }
    /* No construction */
    if( createNewNode ) {
      Blueprint n = Blueprint.newNode( orig, nodes );
      collect( n );
    } else
      collect( b );

  }

  private void collect( Blueprint b ) {
    b.oreC += b.oreR;
    b.clayC += b.clayR;
    b.obsidianC += b.obsidianR;
    b.geodeC += b.geodeR;
  }

  private void reset( Blueprint b ) {
    nodes = new ArrayList<>( List.of( b ) );
    minutes = 0;
  }

  private void treat( boolean part2 ) {

    int maxMinutes = part2 ? 32 : 24;
    for( Blueprint b : blueprints.values() )
      if( !part2 || b.getId() <= 3 ) {
        reset( b );
        while( minutes < maxMinutes ) {
          for( Blueprint n : new ArrayList<>( nodes ) )
            doAction( n, maxMinutes );
          minutes++;
        }
        blueprints.put(
            b.getId(),
            nodes.stream().max( Comparator.comparingInt( Blueprint::getQuality ) ).orElse( b ) );
      }

  }

  @Test
  public void part1() throws IOException {
    readInput();
    treat( false );
    CP.print(
        "Day19, part1, sum of quality levels",
        Map.of( "sum", blueprints.values().stream().mapToInt( Blueprint::getQuality ).sum() ) );
  }

  @Test
  public void part2() throws IOException {
    readInput();
    treat( true );
    CP.print(
        "Day19, part2, geodes numbers",
        Map.of(
            "multiply",
            blueprints.get( 1 ).geodeC
                * blueprints.get( 2 ).geodeC
                * blueprints.get( 3 ).geodeC ) );
  }

}
