package morrison.email.domains.sendgrid;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
public class Send {

    private List<Personalization> personalizations;
    private EmailAddress from;
    @JsonProperty("reply_to")
    private EmailAddress replyTo;
    private List<Content> content;

    public List<Personalization> getPersonalizations() {
        return personalizations;
    }

    public Send setPersonalizations(List<Personalization> personalizations) {
        this.personalizations = personalizations;
        return this;
    }

    public EmailAddress getFrom() {
        return from;
    }

    public Send setFrom(EmailAddress from) {
        this.from = from;
        return this;
    }

    public EmailAddress getReplyTo() {
        return replyTo;
    }

    public Send setReplyTo(EmailAddress replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public List<Content> getContent() {
        return content;
    }

    public Send setContent(List<Content> content) {
        this.content = content;
        return this;
    }
}
