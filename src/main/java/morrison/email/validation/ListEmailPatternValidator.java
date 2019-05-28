package morrison.email.validation;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author Daniel Morrison
 * @since 28/05/2019.
 */
public class ListEmailPatternValidator implements ConstraintValidator<ListOfEmails, List<String>> {

    private final EmailValidator emailValidator = new EmailValidator();

    @Override
    public void initialize(ListOfEmails constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        for (String v : value) {
            if (!emailValidator.isValid(v, context)) {
                return false;
            }
        }
        return true;
    }
}
