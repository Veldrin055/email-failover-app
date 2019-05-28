package morrison.email;

import morrison.email.domains.Email;

import java.util.Collections;

/**
 * @author Daniel Morrison
 * @since 28/05/2019.
 */
public class TestUtils {

    private TestUtils() {
    }

    public static Email buildEmail() {
        return new Email()
                .setTo(Collections.singletonList("to@email.com"))
                .setCc(Collections.singletonList("cc@email.com"))
                .setBcc(Collections.singletonList("bcc@email.com"))
                .setFrom("from@email.com")
                .setSubject("subject")
                .setBody("body");
    }
}
