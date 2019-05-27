package morrison.email.domains.sendgrid;

/**
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
public class EmailAddress {

    private String email;
    private String name;

    public static EmailAddress from(String s) {
        return new EmailAddress().setEmail(s);
    }

    public String getEmail() {
        return email;
    }

    public EmailAddress setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public EmailAddress setName(String name) {
        this.name = name;
        return this;
    }
}
