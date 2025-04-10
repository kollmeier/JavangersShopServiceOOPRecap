package ckollmeier.de.ValidationHelpers;

import ckollmeier.de.ValidationUtils;
import jakarta.validation.constraints.NotNull;

public record NotNullString(
        @NotNull String value
) implements ValidationHelper<String> {
    public static NotNullString generateValidated(final String value) {
        return ValidationUtils.validated(new NotNullString(value));
    }
}
