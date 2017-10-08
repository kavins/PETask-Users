package com.petask.petask_users.error;

/**
 * This class contains the list of error codes defined which are used across the app
 */
public class ErrorCodes {

    public static final int
        CODE_UNKNOWN_HOST_EXCEPTION = 1, // No Internet
        CODE_SOCKET_TIMEOUT = 2, // Slow Internet
        CODE_NO_DATA = 3, // No (more) Data
        CODE_UNKNOWN = 4; // Any other error
}
