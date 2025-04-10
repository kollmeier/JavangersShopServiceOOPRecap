package ckollmeier.de.ValidationHelpers;

import ckollmeier.de.ValidationUtils;
import jakarta.validation.constraints.Pattern;

public record DecimalString(
        @Pattern(regexp = "^-?\\d+(\\.\\d+)?$") String value
) implements ValidationHelper<String> {
    public static DecimalString generateValidated(final String value) {
        return ValidationUtils.validated(new DecimalString(value));
    }
}
