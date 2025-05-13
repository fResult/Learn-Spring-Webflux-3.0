package com.fResult.io.files.io.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

public class FileUtils {
  private FileUtils() {
    throw new IllegalStateException("This cannot be instantiated");
  }

  @SneakyThrows(IOException.class)
  public static File setup() {
    final var file = Files.createTempFile("io-content-data", ".txt").toFile();
    file.deleteOnExit();

    try (var in = new ClassPathResource("/content").getInputStream();
        final var out = new FileOutputStream(file)) {

      FileCopyUtils.copy(in, out);
    }

    return file;
  }
}
