package com.commercetools.sunrise.framework.viewmodels.forms;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public abstract class FormSelectableOptionViewModel extends ViewModel {

    private String label;
    private String value;
    private String description;
    private String image;
    private boolean selected;

    public FormSelectableOptionViewModel() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
