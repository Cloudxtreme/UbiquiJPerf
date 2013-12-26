package com.braindeadprojects.ubiquijperf.ubiquiti;

public class PasswordException extends java.lang.Exception {
	/**
	 * Adding a serialized long
	 */
	private static final long serialVersionUID = 7133602543344011289L;

	public PasswordException()
	{
		this("Username/Password is invalid");
	}
	
	public PasswordException(String message)
	{
		super(message);
	}
}
