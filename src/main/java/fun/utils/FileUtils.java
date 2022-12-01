package fun.utils;

import java.io.FileReader;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

public class FileUtils {

  public static FileReader fromResources( String path ) throws IOException {
    return new FileReader( new ClassPathResource( path ).getFile() );
  }

}
