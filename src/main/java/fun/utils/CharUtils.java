package fun.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CharUtils {

  public static List<Character> asList( char ... chars ) {
    List<Character> list = new ArrayList<>();
    for( char c : chars )
      list.add( c );
    return list;
  }

  public static Set<Character> asSet( char ... chars ) {
    Set<Character> set = new HashSet<>();
    for( char c : chars )
      set.add( c );
    return set;
  }

}
