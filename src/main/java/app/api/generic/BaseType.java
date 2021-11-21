package app.api.generic;

import java.lang.constant.Constable;

public interface BaseType<T extends BaseType<T>> extends Constable, Comparable<T> {
}
