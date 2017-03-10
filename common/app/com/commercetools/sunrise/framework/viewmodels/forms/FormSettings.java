package com.commercetools.sunrise.framework.viewmodels.forms;

public interface FormSettings<T> extends WithFormFieldName {

    T getDefaultValue();

    T mapToValue(final String valueAsString);

    boolean isValidValue(final T value);
}

