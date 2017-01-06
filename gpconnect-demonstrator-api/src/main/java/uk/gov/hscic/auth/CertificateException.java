package uk.gov.hscic.auth;

/**
 * Error thrown if certificate is rejected or missing.
 */
public class CertificateException extends RuntimeException {
    private static final long serialVersionUID = 3569774605210338060L;

    private final int statusCode;

    public CertificateException(String additionalMessage, int statusCode) {
        super("Request is not correctly signed (" + additionalMessage + ')');
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
