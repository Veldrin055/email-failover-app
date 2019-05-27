package morrison.email.domains.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Personalization {

    private List<EmailAddress> to;
    private List<EmailAddress> cc;
    private List<EmailAddress> bcc;
    private String subject;

    public List<EmailAddress> getTo() {
        return to;
    }

    public Personalization setTo(List<EmailAddress> to) {
        this.to = to;
        return this;
    }

    public List<EmailAddress> getCc() {
        return cc;
    }

    public Personalization setCc(List<EmailAddress> cc) {
        this.cc = cc;
        return this;
    }

    public List<EmailAddress> getBcc() {
        return bcc;
    }

    public Personalization setBcc(List<EmailAddress> bcc) {
        this.bcc = bcc;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Personalization setSubject(String subject) {
        this.subject = subject;
        return this;
    }
}
