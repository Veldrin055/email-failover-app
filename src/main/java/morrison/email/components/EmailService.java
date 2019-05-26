package morrison.email.components;

import morrison.email.domains.Email;

/**
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
public interface EmailService {

    boolean sendEmail(Email email);
}
