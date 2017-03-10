package com.commercetools.sunrise.framework.viewmodels.forms;

import java.util.List;

public abstract class AbstractFormSettingsWithOptions<T extends FormOption> extends AbstractFormFieldName implements FormSettingsWithOptions<T> {

    private final List<T> options;

    protected AbstractFormSettingsWithOptions(final String fieldName, final List<T> options) {
        super(fieldName);
        this.options = options;
    }

    @Override
    public List<T> getOptions() {
        return options;
    }
}
