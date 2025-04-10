package ckollmeier.de.ValidationHelpers;

import ckollmeier.de.ValidationUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PositiveOrZeroBigDecimal(
        @PositiveOrZero(message = "Value must be positive or zero") BigDecimal value
) implements ValidationHelper<BigDecimal> {
    public static PositiveOrZeroBigDecimal generateValidated(final BigDecimal value) {
        return ValidationUtils.validated(new PositiveOrZeroBigDecimal(value));
    }

    public static PositiveOrZeroBigDecimal generateValidated(final @Pattern(regexp = "^-?\\d+(\\.\\d+)?$") @NotBlank String value) {
        return generateValidated(new BigDecimal(DecimalString.generateValidated(value).value()));
    }
}
