package com.tiemagolf.rxbus.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the tags for a subscriber, as used by {@link com.tiemagolf.rxbus.finder.AnnotatedFinder} and {@link com.tiemagolf.rxbus.Bus}.
 * <p>The tag's default value is {@code Tag.DEFAULT}.
 * <p>If this annotation is applied to subscriber with none parameter or more than one parameter, Bus will
 * delivery the events(tag and method's first (and only) parameter).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Tag {
    static final String DEFAULT = "rxbus_default_tag";

    String value() default DEFAULT;
}
