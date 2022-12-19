package fun.utils;

import java.awt.*;

public class InputUtils {

  public static Point sToPoint( String point ) {
    String[] coordinates = point.split( "," );
    return new Point( Integer.parseInt( coordinates[0] ), Integer.parseInt( coordinates[1] ) );
  }

}
