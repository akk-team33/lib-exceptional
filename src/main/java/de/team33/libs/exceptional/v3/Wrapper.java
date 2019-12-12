package de.team33.libs.exceptional.v3;

import java.util.function.BiFunction;

/**
 * @deprecated use {@link RuntimeWrapper} instead.
 */
@Deprecated
public class Wrapper<X extends RuntimeException> extends RuntimeWrapper<X> {

    public static final Wrapper<WrappedException> DEFAULT = new Wrapper<>(WrappedException::new);

    /**
     * Initiates a new instance with a function that provides a wrapping runtime exception.
     */
    public Wrapper(final BiFunction<String, Throwable, X> wrapping) {
        super(wrapping);
    }
}
