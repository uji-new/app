package app.api.generic;

public interface BaseType<T extends Enum<T> & BaseType<T>> {
}
