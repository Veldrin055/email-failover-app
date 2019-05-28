package morrison.email.domains;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * App domain email object
 *
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Email {

    @javax.validation.constraints.Email
    private List<String> cc;
    @javax.validation.constraints.Email
    private List<String> to;
    @javax.validation.constraints.Email
    private List<String> bcc;
    @javax.validation.constraints.Email
    private String from;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;

    public List<String> getTo() {
        return Optional.ofNullable(to).orElseGet(Collections::emptyList);
    }

    public Email setTo(List<String> to) {
        this.to = to;
        return this;
    }

    public List<String> getCc() {
        return Optional.ofNullable(cc).orElseGet(Collections::emptyList);
    }

    public Email setCc(List<String> cc) {
        this.cc = cc;
        return this;
    }

    public List<String> getBcc() {
        return Optional.ofNullable(bcc).orElseGet(Collections::emptyList);
    }

    public Email setBcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Email setFrom(String from) {
        this.from = from;
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
