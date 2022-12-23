package fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fun.utils.CP;
import fun.utils.FileUtils;
import fun.utils.InputUtils;
import fun.utils.InputUtils.Point3D;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = Application.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Day18Test {

  private static final String FILE = "day18.txt";

  private List<Point3D> cubs = new ArrayList<>();
  private final List<Point3D> emptyCubs = new ArrayList<>();
  private final Set<Point3D> trappedCubs = new HashSet<>();
  // LiberalCubs not mandatory, but using it, we gain 2 second,
  // (total 1.3s instead of 3.3s)
  private final Set<Point3D> liberalCubs = new HashSet<>();

  private void readInput() throws IOException {
    cubs = FileUtils.fromResourcesAsStream( FILE ).map( InputUtils::sTo3DPoint )
        .collect( Collectors.toList() );
  }

  private boolean areAdjacent( Point3D p1, Point3D p2 ) {
    return Math.abs( p1.x - p2.x ) + Math.abs( p1.y - p2.y ) + Math.abs( p1.z - p2.z ) == 1;
  }

  private int getEntireSurface( Collection<Point3D> coll ) {
    int surface = 0;
    for( Point3D point : coll )
      surface += 6 - coll.stream().filter( p -> areAdjacent( p, point ) ).count();
    return surface;
  }

  private List<Point3D> getAdjacentCubs( Point3D point ) {
    return List.of(
        new Point3D( point.x - 1, point.y, point.z ),
        new Point3D( point.x + 1, point.y, point.z ),
        new Point3D( point.x, point.y - 1, point.z ),
        new Point3D( point.x, point.y + 1, point.z ),
        new Point3D( point.x, point.y, point.z - 1 ),
        new Point3D( point.x, point.y, point.z + 1 ) );
  }

  private List<Point3D> getAdjacentEmptyCubs( Point3D point ) {
    return getAdjacentCubs( point ).stream().filter( emptyCubs::contains )
        .collect( Collectors.toList() );
  }

  @Test
  public void part1() throws IOException {
    readInput();
    CP.print(
        "Day18, part1, surface area of scanned lava droplet",
        Map.of( "surface", getEntireSurface( cubs ) ) );
  }

  @Test
  public void part2() throws IOException {
    readInput();

    // Get borders
    int minX = cubs.stream().mapToInt( Point3D::getX ).min().orElse( 0 );
    int maxX = cubs.stream().mapToInt( Point3D::getX ).max().orElse( 0 );
    int minY = cubs.stream().mapToInt( Point3D::getY ).min().orElse( 0 );
    int maxY = cubs.stream().mapToInt( Point3D::getY ).max().orElse( 0 );
    int minZ = cubs.stream().mapToInt( Point3D::getZ ).min().orElse( 0 );
    int maxZ = cubs.stream().mapToInt( Point3D::getZ ).max().orElse( 0 );

    // EmptyCubs, Consider larger border (min -1, max +1)
    // so getAdjacentEmptyCubs includes one outside
    for( int x = minX - 1; x <= maxX + 1; x++ )
      for( int y = minY - 1; y <= maxY + 1; y++ )
        for( int z = minZ - 1; z <= maxZ + 1; z++ ) {
          Point3D pt = new Point3D( x, y, z );
          if( !cubs.contains( pt ) )
            emptyCubs.add( pt );
        }

    // TrappedCubs
    for( Point3D empty : emptyCubs ) {

      if( trappedCubs.contains( empty ) || liberalCubs.contains( empty ) )
        continue;

      Set<Point3D> airVolume = new HashSet<>( getAdjacentEmptyCubs( empty ) );
      airVolume.add( empty );

      boolean isTrapped = true;
      boolean moveMore = true;
      while( moveMore ) {

        Set<Point3D> newPossibleMoves = new HashSet<>();
        for( Point3D pp : new ArrayList<>( airVolume ) )
          newPossibleMoves.addAll(
              getAdjacentEmptyCubs( pp ).stream().filter( s -> !airVolume.contains( s ) )
                  .collect( Collectors.toSet() ) );

        moveMore = airVolume.addAll( newPossibleMoves );

        if( newPossibleMoves.stream().anyMatch(
            copy -> copy.x >= maxX
                || copy.x <= minX
                || copy.y >= maxY
                || copy.y <= minY
                || copy.z >= maxZ
                || copy.z <= minZ ) ) {
          isTrapped = false;
          moveMore = false;
        }

      }

      if( isTrapped )
        trappedCubs.addAll( airVolume );
      else
        liberalCubs.addAll( airVolume );

    }

    CP.print(
        "Day18, part2, exterior surface area of scanned lava droplet",
        Map.of( "surface", getEntireSurface( cubs ) - getEntireSurface( trappedCubs ) ) );
  }

}
