package morrison.email.exceptions;

/**
 * Business exception to be thrown in the case of an email service provider (eg sendgrid, mailgun) failing (non 2xx
 * returned).
 *
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
public class EmailServiceException extends Throwable {
    public EmailServiceException(String message) {
        super(message);
    }
}
