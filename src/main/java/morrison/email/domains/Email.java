package morrison.email.domains;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    private List<@javax.validation.constraints.Email(message = "'cc' must be a valid email address") String> cc;
    @NotEmpty(message = "'to' field must not be empty")
    private List<@javax.validation.constraints.Email(message = "'to' must be a valid email address") String> to;
    private List<@javax.validation.constraints.Email(message = "'bcc' must be a valid email address") String> bcc;
    @javax.validation.constraints.Email(message = "'from' must be a valid email address")
    @NotBlank(message = "'from' must not be blank")
    private String from;
    @NotBlank(message = "'subject' must not be blank")
    private String subject;
    @NotBlank(message = "'body' must not be blank")
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
