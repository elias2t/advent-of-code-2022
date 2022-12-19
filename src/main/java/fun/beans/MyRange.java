package fun.beans;

public class MyRange implements Comparable<MyRange> {

  public int start;

  public int end;

  public MyRange( int start, int end ) {
    this.start = start;
    this.end = end;
  }

  public int distance() {
    return end - start + 1;
  }

  public boolean contains( int v ) {
    return v >= start && v <= end;
  }

  public boolean isMergePossible( MyRange other ) {

    boolean merged = false;
    if( other.start <= this.start && other.end >= this.start ) {
      this.start = other.start;
      merged = true;
    }
    if( other.start >= this.start && other.end <= this.end ) {
      merged = true;
    }
    if( other.start <= this.end && other.end >= this.end ) {
      this.end = other.end;
      merged = true;
    }
    return merged;

  }

  @Override
  public int compareTo( MyRange o ) {
    return Integer.compare( this.start, o.start );
  }

  @Override
  public String toString() {
    return start + "_" + end;
  }

}
