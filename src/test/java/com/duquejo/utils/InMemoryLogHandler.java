/* (C) @duquejo 2025 */
package com.duquejo.utils;

import static org.wildfly.common.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class InMemoryLogHandler extends Handler {

  private final Predicate<LogRecord> predicate;
  private final List<LogRecord> records = new ArrayList<>();

  public InMemoryLogHandler(Predicate<LogRecord> predicate) {
    this.predicate = checkNotNullParam("predicate", predicate);
  }

  public List<LogRecord> getRecords() {
    return records;
  }

  @Override
  public void publish(LogRecord rec) {
    if (predicate.test(rec)) {
      records.add(rec);
    }
  }

  @Override
  public void flush() {
    // Not implemented method
  }

  @Override
  public void close() throws SecurityException {
    this.records.clear();
  }
}
