package ckollmeier.de;

import ckollmeier.de.ValidationHelpers.ValidationHelper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.function.Function;

public final class ValidatedInput {
    public static final Scanner SCANNER = new Scanner(System.in);

    private ValidatedInput() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Prompts the user for input, validates it, and returns the validated result.
     *
     * @param <T>      the type of the input value
     * @param message  the message to display to the user
     * @param generate a function that generates a {@link ValidationHelper} to validate the input
     * @return the validated input value
     */
    public static <T> T getValidatedInput(final @NonNull String message, final @NonNull Function<String, ValidationHelper<T>> generate) {
        while (true) {
            System.out.print("\33[s");
            System.out.println(message);
            try {
                return generate.apply(SCANNER.nextLine()).value();
            } catch (ConstraintViolationException cve) {
                handleValidationException(cve);
            }
        }
    }

    /**
     * Prompts the user for input, validates it using a static method in the given class,
     * and returns the validated result.
     *
     * @param <T>     the type of the input value
     * @param message the message to display to the user
     * @param clazz   the {@link Class} object representing the {@link ValidationHelper} implementation
     * @return the validated input value
     * @throws IllegalArgumentException if the required validation method is not found or cannot be accessed
     */
    public static <T> T getValidatedInput(final @NonNull String message, final @NonNull Class<? extends ValidationHelper<T>> clazz) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("generateValidated", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        return getValidatedInput(message, asFunction(method, null));
    }

    private static <T, R> Function<T, R> asFunction(final Method method, final Object targetInstance) {
        return (T t) -> {
            try {
                @SuppressWarnings("unchecked")
                R result = (R) method.invoke(targetInstance, t);
                return result;
            } catch (InvocationTargetException e) {
                Throwable cause = e.getTargetException();
                if (cause instanceof ConstraintViolationException cve) {
                    throw cve;
                } else if (cause instanceof RuntimeException re) {
                    throw re;
                } else {
                    throw new RuntimeException("Unexpected exception during method invocation", cause);
                }
            } catch (Exception e) {
                throw new RuntimeException("Reflection error during method access", e);
            }
        };
    }

    private static void handleValidationException(final ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
        System.out.print("\u001B[31m" + message + "\u001B[0m");
        System.out.print("\33[u");
    }
}
