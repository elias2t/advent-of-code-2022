package fun.utils;

import java.awt.*;

import lombok.AllArgsConstructor;
import lombok.Data;

public class InputUtils {

  public static Point sToPoint( String point ) {
    String[] coordinates = point.split( "," );
    return new Point( Integer.parseInt( coordinates[0] ), Integer.parseInt( coordinates[1] ) );
  }

  public static Point3D sTo3DPoint( String point ) {
    String[] coordinates = point.split( "," );
    return new Point3D(
        Integer.parseInt( coordinates[0] ),
        Integer.parseInt( coordinates[1] ),
        Integer.parseInt( coordinates[2] ) );
  }

  @Data
  @AllArgsConstructor
  public static class Point3D {

    public int x;

    public int y;

    public int z;

  }

}
