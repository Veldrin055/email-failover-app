package morrison.email.domains.sendgrid;

/**
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
public class Content {
    private String type;
    private String value;

    public String getType() {
        return type;
    }

    public Content setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Content setValue(String value) {
        this.value = value;
        return this;
    }
}
