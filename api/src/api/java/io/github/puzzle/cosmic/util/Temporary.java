package io.github.puzzle.cosmic.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.Void.TYPE;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Temporary {
}
