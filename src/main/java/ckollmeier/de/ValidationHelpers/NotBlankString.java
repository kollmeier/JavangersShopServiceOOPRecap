package ckollmeier.de.ValidationHelpers;

import ckollmeier.de.ValidationUtils;
import jakarta.validation.constraints.NotBlank;

public record NotBlankString(
        @NotBlank String value
) implements ValidationHelper<String> {
    public static NotBlankString generateValidated(final String value) {
        return ValidationUtils.validated(new NotBlankString(value));
    }
}
