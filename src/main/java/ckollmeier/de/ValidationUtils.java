package ckollmeier.de;

// Utility-Klasse zur zentralen Validierung beliebiger Objekte

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public final class ValidationUtils {
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    /**
     * Validiert ein beliebiges Objekt anhand seiner Constraint-Annotations.
     * @param object das zu validierende Objekt
     * @param <T> Class of the object
     * @throws ConstraintViolationException wenn eine oder mehrere Constraints verletzt wurden
     */
    public static <T> void validate(final T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation failed", Set.copyOf(violations));
        }
    }

    /**
     * Validates the provided object using its constraint annotations and returns it.
     *
     * @param object the object to validate
     * @param <T>    the type of the object
     * @return the validated object
     * @throws ConstraintViolationException if one or more constraints are violated
     */
    public static <T> T validated(final T object) {
        validate(object);
        return object;
    }
}

// Beispielhafte Verwendung:
// ValidationUtils.validate(order);
// → wirft eine Exception bei Fehlern, sonst läuft es normal weiter
