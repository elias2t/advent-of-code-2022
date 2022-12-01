package fun.utils;

import java.util.Map;
import java.util.Map.Entry;

/** ConsolePrinter */
public class CP {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_CYAN = "\u001B[36m";

  public static void print( String msg, Map<String, Object> map ) {

    System.out.print( "\n" );

    System.out.print( ANSI_GREEN + msg + "::" + ANSI_RESET );
    for( Entry entry : map.entrySet() )
      System.out.print( " " + entry.getKey() + ": " + ANSI_CYAN + entry.getValue() + ANSI_RESET );
    System.out.print( "\n\n" );

  }

}
