package fun.utils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;

public class FileUtils {

  public static FileReader fromResources( String path ) throws IOException {
    return new FileReader( new ClassPathResource( path ).getFile() );
  }

  public static Stream<String> fromResourcesAsStream( String path ) throws IOException {
    return Files.lines( new ClassPathResource( path ).getFile().toPath() );
  }

  public static List<String> fromResourcesAsList( String path ) throws IOException {
    return Files.lines( new ClassPathResource( path ).getFile().toPath() )
        .collect( Collectors.toList() );
  }

}
