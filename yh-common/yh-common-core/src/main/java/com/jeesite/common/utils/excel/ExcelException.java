/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel;

/**
 * Excel Exception
 * @author Winhai
 */
public class ExcelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcelException() {
		super();
	}

	public ExcelException(String message) {
		super(message);
	}

	public ExcelException(Throwable cause) {
		super(cause);
	}

	public ExcelException(String message, Throwable cause) {
		super(message, cause);
	}
}
