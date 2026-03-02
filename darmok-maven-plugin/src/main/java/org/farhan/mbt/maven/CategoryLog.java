package org.farhan.mbt.maven;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.maven.plugin.logging.Log;

public class CategoryLog implements Log, Closeable {

	private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	private final Log delegate;
	private final PrintWriter writer;
	private final String category;

	public CategoryLog(Log delegate, String category, Path logFile) throws IOException {
		this.delegate = delegate;
		this.category = category;
		Files.createDirectories(logFile.getParent());
		this.writer = new PrintWriter(new FileWriter(logFile.toFile(), true), true);
	}

	private void writeLine(String level, CharSequence content) {
		writer.println(LocalDateTime.now().format(TIMESTAMP) + " " + level + " [" + category + "] " + content);
	}

	private void writeLine(String level, CharSequence content, Throwable error) {
		writeLine(level, content);
		error.printStackTrace(writer);
	}

	@Override
	public boolean isDebugEnabled() {
		return delegate.isDebugEnabled();
	}

	@Override
	public void debug(CharSequence content) {
		delegate.debug(content);
		writeLine("DEBUG", content);
	}

	@Override
	public void debug(CharSequence content, Throwable error) {
		delegate.debug(content, error);
		writeLine("DEBUG", content, error);
	}

	@Override
	public void debug(Throwable error) {
		delegate.debug(error);
		writeLine("DEBUG", "", error);
	}

	@Override
	public boolean isInfoEnabled() {
		return delegate.isInfoEnabled();
	}

	@Override
	public void info(CharSequence content) {
		delegate.info(content);
		writeLine("INFO ", content);
	}

	@Override
	public void info(CharSequence content, Throwable error) {
		delegate.info(content, error);
		writeLine("INFO ", content, error);
	}

	@Override
	public void info(Throwable error) {
		delegate.info(error);
		writeLine("INFO ", "", error);
	}

	@Override
	public boolean isWarnEnabled() {
		return delegate.isWarnEnabled();
	}

	@Override
	public void warn(CharSequence content) {
		delegate.warn(content);
		writeLine("WARN ", content);
	}

	@Override
	public void warn(CharSequence content, Throwable error) {
		delegate.warn(content, error);
		writeLine("WARN ", content, error);
	}

	@Override
	public void warn(Throwable error) {
		delegate.warn(error);
		writeLine("WARN ", "", error);
	}

	@Override
	public boolean isErrorEnabled() {
		return delegate.isErrorEnabled();
	}

	@Override
	public void error(CharSequence content) {
		delegate.error(content);
		writeLine("ERROR", content);
	}

	@Override
	public void error(CharSequence content, Throwable error) {
		delegate.error(content, error);
		writeLine("ERROR", content, error);
	}

	@Override
	public void error(Throwable error) {
		delegate.error(error);
		writeLine("ERROR", "", error);
	}

	@Override
	public void close() {
		writer.close();
	}
}
