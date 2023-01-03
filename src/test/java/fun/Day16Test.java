package fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day16Test {

  private static final String FILE = "day16.txt";

  private final Map<String, Valve> valves = new HashMap<>();

  private final LinkedList<DataSet> data = new LinkedList<>();

  private int ratesSum;

  private int sizeToOpen = 0;

  public int minutes = 0;

  private void readInput() throws IOException {

    Iterator<String> it = FileUtils.fromResourcesAsStream( FILE ).iterator();
    while( it.hasNext() ) {
      String[] data = it.next().replace( "Valve ", "" ).replace( " has flow rate", "" )
          .replace( " tunnels lead to valves ", "" ).replace( " tunnel leads to valve ", "" )
          .split( ";" );
      Valve v = new Valve( data[0].split( "=" ), data[1].split( ", " ) );
      valves.put( v.getId(), v );
    }
    sizeToOpen = (int) valves.values().stream().filter( v -> v.getRate() > 0 ).count();
    ratesSum = valves.values().stream().mapToInt( Valve::getRate ).sum();
    valves.values().forEach( v -> v.setSubObjects( valves ) );

  }

  @Data
  public static class Valve {

    private final String id;

    private final int rate;

    private final List<String> connections;

    private List<Valve> subs = new ArrayList<>();

    public Valve( String[] valve, String[] tunnels ) {
      this.id = valve[0];
      this.rate = Integer.parseInt( valve[1] );
      this.connections = Arrays.asList( tunnels );
    }

    public void setSubObjects( Map<String, Valve> valves ) {
      for( String id : connections )
        subs.add( valves.get( id ) );
    }

  }

  @AllArgsConstructor
  public static class DataSet {

    @Getter
    public int pressureTotal = 0;

    public int pressureByMinute = 0;

    public Set<String> openedValves = new HashSet<>();

    public Valve current;

    public Valve currentElephant;

    public Set<String> valuesSinceLastOpen = new HashSet<>();

    public Set<String> valuesSinceLastOpenElephant = new HashSet<>();

    public DataSet( Valve valve ) {
      this.current = valve;
      this.currentElephant = valve;
    }

    public DataSet( DataSet from, @Nullable Valve me, @Nullable Valve elephant ) {
      this.pressureTotal = from.pressureTotal;
      this.pressureByMinute = from.pressureByMinute;
      this.openedValves = new HashSet<>( from.openedValves );
      this.valuesSinceLastOpen = new HashSet<>( from.valuesSinceLastOpen );
      this.valuesSinceLastOpenElephant = new HashSet<>( from.valuesSinceLastOpenElephant );
      this.current = me != null ? me : from.current;
      this.currentElephant = elephant != null ? elephant : from.currentElephant;
    }

  }

  private boolean openAction( DataSet d, boolean isMe ) {
    Valve current = isMe ? d.current : d.currentElephant;
    d.pressureByMinute += current.getRate();
    d.openedValves.add( current.getId() );
    if( isMe )
      d.valuesSinceLastOpen = new HashSet<>();
    else
      d.valuesSinceLastOpenElephant = new HashSet<>();
    return true;
  }

  private void part1MoveActions( DataSet beforeOpen, DataSet afterOpen, boolean createNewRoad ) {
    // if all are opened don't move
    if( afterOpen.openedValves.size() != sizeToOpen )
      for( Valve n : beforeOpen.current.subs )
        if( !beforeOpen.valuesSinceLastOpen.contains( n.getId() ) )
          if( createNewRoad ) {
            data.add( new DataSet( beforeOpen, n, null ) );
          } else {
            afterOpen.current = n;
            createNewRoad = true;
          }
  }

  private void part2MoveActions( DataSet beforeOpen, DataSet afterOpen, boolean createNewRoad ) {
    // if all are opened don't move
    if( afterOpen.openedValves.size() != sizeToOpen )
      for( Valve n : beforeOpen.current.subs )
        for( Valve nElephant : beforeOpen.currentElephant.subs )
          if( !beforeOpen.valuesSinceLastOpen.contains( n.getId() )
              && !beforeOpen.valuesSinceLastOpenElephant.contains( nElephant.getId() )
              && !n.getId().equals( nElephant.getId() ) )
            if( createNewRoad ) {
              data.add( new DataSet( beforeOpen, n, nElephant ) );
            } else {
              afterOpen.current = n;
              afterOpen.currentElephant = nElephant;
              createNewRoad = true;
            }
  }

  private void part2MoveBActions( DataSet beforeOpen, DataSet afterOpen, boolean meOpen ) {
    if( afterOpen.openedValves.size() != sizeToOpen )
      if( meOpen ) {
        for( Valve nElephant : beforeOpen.currentElephant.subs )
          if( !beforeOpen.valuesSinceLastOpenElephant.contains( nElephant.getId() )
              && !afterOpen.current.getId().equals( nElephant.getId() ) )
            data.add( new DataSet( afterOpen, null, nElephant ) );
      } else {
        for( Valve n : beforeOpen.current.subs )
          if( !beforeOpen.valuesSinceLastOpen.contains( n.getId() )
              && !afterOpen.currentElephant.getId().equals( n.getId() ) )
            data.add( new DataSet( afterOpen, n, null ) );
      }
  }

  private void doAction( DataSet d, boolean part2 ) {

    d.pressureTotal += d.pressureByMinute;
    d.valuesSinceLastOpen.add( d.current.getId() );
    d.valuesSinceLastOpenElephant.add( d.currentElephant.getId() );
    Valve current = d.current;
    Valve currentElephant = d.currentElephant;
    boolean createNewRoad = false;
    boolean createNewRoadElephant = false;
    DataSet beforeOpen = new DataSet( d, null, null );

    if( !part2 ) {
      if( current.getRate() > 0 && !d.openedValves.contains( current.getId() ) )
        createNewRoad = openAction( d, true );
      part1MoveActions( beforeOpen, d, createNewRoad );
    } else {

      DataSet d1 = new DataSet( d, null, null ); // duplicate d
      DataSet d2 = new DataSet( d, null, null ); // duplicate d

      if( current.getRate() > 0 && !d1.openedValves.contains( current.getId() ) ) {
        createNewRoad = openAction( d1, true );
        openAction( d, true );
      }
      if( currentElephant.getRate() > 0
          && !d2.openedValves.contains( currentElephant.getId() )
          && !current.getId().equals( currentElephant.getId() ) ) {
        createNewRoadElephant = openAction( d2, false );
        openAction( d, false );
      }

      if( createNewRoad )
        part2MoveBActions( beforeOpen, d1, true );
      if( createNewRoadElephant )
        part2MoveBActions( beforeOpen, d2, false );
      part2MoveActions( beforeOpen, d, createNewRoad || createNewRoadElephant );

    }

  }

  private int treat( boolean part2 ) {

    DataSet best = new DataSet( valves.get( "AA" ) );
    data.add( best );

    int minutesMax = part2 ? 26 : 30;
    while( minutes < minutesMax ) {
      minutes++;

      // reduce
      int bestP = best.pressureTotal;
      if( minutes > minutesMax / 4 )
        data.removeIf( v -> v.pressureTotal < bestP - ratesSum / 2 );
      if( minutes > minutesMax / 2 )
        data.removeIf( v -> v.pressureTotal < bestP - ratesSum / 3 );
      if( minutes > minutesMax * 3 / 4 )
        data.removeIf( v -> v.pressureTotal < bestP - ratesSum / 4 );

      // develop
      for( DataSet d : new ArrayList<>( data ) ) {
        doAction( d, part2 );
        if( d.pressureTotal > best.pressureTotal )
          best = d;
      }

    }
    return best.pressureTotal;

  }

  @Test
  public void part1() throws IOException {
    readInput();
    CP.print(
        "Day16, part1, most pressure that can be released",
        Map.of( "pressure", treat( false ) ) ); // 1915
  }

  @Test
  public void part2() throws IOException {
    readInput();
    CP.print(
        "Day16, part2, most pressure that can be released",
        Map.of( "pressure", treat( true ) ) ); // 2772
  }

}