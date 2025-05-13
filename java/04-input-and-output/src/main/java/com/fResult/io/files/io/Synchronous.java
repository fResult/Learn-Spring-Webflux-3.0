package com.fResult.io.files.io;

import com.fResult.io.files.io.files.FileUtils;
import java.io.*;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Synchronous {
  @SneakyThrows(IOException.class)
  static void read(File file, Consumer<byte[]> bytesConsumer) {
    try (final var in = new BufferedInputStream(new FileInputStream(file));
        var out = new ByteArrayOutputStream()) {

      var read = -1;
      final var bytes = new byte[1024];
      while ((read = in.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }

      bytesConsumer.accept(out.toByteArray());
    }
  }

  public static void main(String... args) {
    final var file = FileUtils.setup();
    log.info("file read start");
    read(file, bytes -> log.info("read {} and the file is {}", bytes.length, file.length()));
    log.info("file read stop");
  }
}
