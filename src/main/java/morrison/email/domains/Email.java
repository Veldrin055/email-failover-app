package morrison.email.domains;

import java.util.List;

/**
 * App domain email object
 *
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
public class Email {

    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String body;

    public List<String> getTo() {
        return to;
    }

    public Email setTo(List<String> to) {
        this.to = to;
        return this;
    }

    public List<String> getCc() {
        return cc;
    }

    public Email setCc(List<String> cc) {
        this.cc = cc;
        return this;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public Email setBcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Email setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Email setBody(String body) {
        this.body = body;
        return this;
    }
}
