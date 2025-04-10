package ckollmeier.de.ValidationHelpers;

import ckollmeier.de.Enum.UnitEnum;
import ckollmeier.de.ValidationUtils;
import jakarta.validation.constraints.NotNull;

public record NotNullUnitEnum(
        @NotNull UnitEnum value
) implements ValidationHelper<UnitEnum> {
    /**
     * Erzeugt eine validierte Instanz von {@link NotNullUnitEnum}.
     *
     * @param input Der Eingabewert als String.
     * @return Eine validierte Instanz von {@link NotNullUnitEnum}.
     */
    public static NotNullUnitEnum generateValidated(final String input) {
        UnitEnum result;
        try {
            result = UnitEnum.valueOf(input);
        } catch (IllegalArgumentException e) {
            result = UnitEnum.getUnitForShort(input);
        }
        return ValidationUtils.validated(new NotNullUnitEnum(result));
    }
}
