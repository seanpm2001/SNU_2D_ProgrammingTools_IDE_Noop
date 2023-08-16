package noop.persistence;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Alex Eagle (alexeagle@google.com)
 */
@BindingAnnotation
@Retention(RUNTIME)
public @interface XmlDiskCache {
}
