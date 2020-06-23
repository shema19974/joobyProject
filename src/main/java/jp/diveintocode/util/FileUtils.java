package jp.diveintocode.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileUtils {
  public static void writeAll(String fileName, String text) throws IOException {
    FileWriter fw = new FileWriter(fileName);
    fw.write(text);
    fw.close();
  }

  public static void delete(String fileName) {
    File file = new File(fileName); // [2]

    if (!file.exists()) {
      return;
    }
    if (file.delete()) {
      return;
    }
  }

  public static String readAll(final String path) throws IOException {
    return Files.lines(Paths.get(path), Charset.forName("UTF-8"))
        .collect(Collectors.joining(System.getProperty("line.separator")));
  }
}
