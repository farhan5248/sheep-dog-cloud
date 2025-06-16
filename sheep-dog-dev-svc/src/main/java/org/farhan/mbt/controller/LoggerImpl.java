package org.farhan.mbt.controller;

import org.slf4j.Logger;

public class LoggerImpl implements org.farhan.mbt.convert.Logger {

	private Logger log;

	public LoggerImpl(Logger log) {
		this.log = log;
	}

	@Override
	public void debug(String message) {
		log.debug(message);
	}

}
