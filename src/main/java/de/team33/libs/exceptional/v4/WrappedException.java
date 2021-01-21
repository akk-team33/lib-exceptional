package de.team33.libs.exceptional.v4;

/**
 * A {@link RuntimeException} dedicated to wrap other exceptions, especially checked exceptions.
 */
public class WrappedException extends RuntimeException {

  /**
   * Initializes a new instance with the given message and cause.
   */
    public WrappedException(final String message, final Throwable cause) {
        super(message, cause);
    }

  /**
   * Initializes a new instance with the given cause and its {@link Throwable#getMessage() message}.
   */
  public WrappedException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
