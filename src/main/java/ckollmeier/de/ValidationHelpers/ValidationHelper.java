package ckollmeier.de.ValidationHelpers;

@FunctionalInterface
public interface ValidationHelper<T> {
    static <T> T generateValidated(final T value) {
        return value;
    }

    T value();
}
