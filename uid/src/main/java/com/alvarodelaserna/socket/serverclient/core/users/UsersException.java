package com.alvarodelaserna.socket.serverclient.core.users;

public class UsersException extends Exception {

	public static final int SERVER_ERROR = 0;
	public static final int NOT_FOUND_DEVICE_CODE = 1;
	public static final int DUPLICATED_USER_CODE = 2;
	public static final int USER_NOT_FOUND_CODE = 3;
	public static final int NOT_LOCALLY_STORED_USER_ID_CODE = 4;
	public static final int NOT_LOCALLY_STORED_EMAIL_CODE = 5;
	public static final int CRYPTO_USER_NOT_AUTHENTICATED_CODE = 7;
	public static final int PHONE_TOO_LONG_CODE = 8;
	public static final int PHONE_TOO_SHORT_CODE = 14;
	public static final int DEVICE_ASSIGNED_USER_CODE = 9;
	public static final int PAGE_LIMIT_REACHED_CODE = 10;
	public static final int BAD_CONNECTION_CODE = 11;
	public static final int TICKET_TIMED_OUT_CODE = 12;
	public static final int WRONG_TICKET_CODE = 13;

	public int errCode = -1;

	public UsersException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	public UsersException(Throwable cause, String message) {
		super(message, cause);
	}

	public UsersException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	public UsersException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
