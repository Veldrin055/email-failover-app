# email-failover-app
Demo simple email integration app, with failover

## How it works
It's a Spring 5 non-blocking app that is configured to use two email services. It makes use of Spring Boot for simple
app bootstrapping. It exposes a endpoint that can be called via HTTP POST in order to send an email. It will first attempt
to use the primary configured email service provider, and if that fails it will attempt the second. If that also fails
it will return with a non 2xx code.

## Build and run
You'll need Gradle 4.4+ installed.

To build: `gradle build`

To run: `gradle bootRun`

To run tests: `gradle test`

## Send an email
Make a HTTP POST request to `/send`. For example, if the server is running locally on port 8080 the full URL will be `http://localhost:8080/send`.
The endpoint consumes a JSON object in the body.

### Request body
* **to**: *required* A list of email addresses to send the email to
* **cc** *optional* A list of email addresses to send the email in cc to
* **bcc** *optional* A list of email addresses to send the email in bcc (blind carbon copy) to
* **from** *required* An email address of the sender. Replies will be directed to this address.
* **subject** *required* The email subject line
* **body** *required* The email body contents, in plain text

Example:

```json
{
  "to": ["first@email.com", "second@email.com"],
  "cc": ["cc@email.com"],
  "from": "sender@email.com",
  "subject": "Check out this example email!",
  "body": "Not bad huh?"
}
```

### Response
A JSON object describing the result of the transaction.
```json
{
  "status": 200,
  "message": "Email sent successfully"
}
```

#### Response codes
* `200 OK` The email was sent by a Email Service Provider successfully.
* `400 CLIENT ERROR` The request was not valid, check the message in the response object for why
* `406 NOT ACCEPTABLE` A field(s) in the request body does not pass validation, check the message in the response for a hint
* `500 SERVER ERROR` Something went wrong in the app, and the email could not be sent

## Configuration
Config lives in `/resources/application.yaml`.

## Improvements that could be made
* Better failover logic - at the moment it's just try primary, and if that fails try secondary. 
* Potentially throttling the calls to the email service providers to prevent flooding them
* Instead of static primary/secondary email service providers configured at bootstrap, there could be a collection assigned. 
This would allow for more than 2 email services.
* More informative response information on what has happened, both good and bad
* Generic HTTP REST email client. They both follow a similar pattern: Map request JSON to vendor request body, and dispatch.
I could use generics to abstract this further.
* Simple web page with a form for creating an email **EDIT** I created a simple one at `/index.html`, it doesn't show
do much except post the form - no response apart from the console, or input validation
* Better input validation, with more informative responses
