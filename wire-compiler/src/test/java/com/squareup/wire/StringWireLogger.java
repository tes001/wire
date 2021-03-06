package com.squareup.wire;

import com.google.common.collect.Iterables;
import com.squareup.javapoet.JavaFile;
import com.squareup.kotlinpoet.FileSpec;
import com.squareup.kotlinpoet.TypeSpec;
import java.nio.file.Path;

final class StringWireLogger implements WireLogger {
  private boolean quiet;
  private StringBuilder buffer = new StringBuilder();

  @Override public void setQuiet(boolean quiet) {
    this.quiet = quiet;
  }

  @Override public synchronized void artifact(Path outputPath, JavaFile javaFile) {
    buffer.append(outputPath);
    buffer.append(" ");
    buffer.append(javaFile.packageName);
    buffer.append(".");
    buffer.append(javaFile.typeSpec.name);
    buffer.append('\n');
  }

  @Override public synchronized void artifact(Path outputPath, FileSpec kotlinFile) {
    TypeSpec typeSpec = (TypeSpec) Iterables.getOnlyElement(kotlinFile.getMembers());
    buffer.append(outputPath);
    buffer.append(" ");
    buffer.append(kotlinFile.getPackageName());
    buffer.append(".");
    buffer.append(typeSpec.getName());
    buffer.append('\n');
  }

  @Override public synchronized void info(String message) {
    if (!quiet) {
      buffer.append(message);
      buffer.append('\n');
    }
  }

  public String getLog() {
    return buffer.toString();
  }
}
