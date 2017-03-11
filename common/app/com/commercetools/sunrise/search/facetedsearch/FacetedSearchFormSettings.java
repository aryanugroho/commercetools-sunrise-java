package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.framework.viewmodels.forms.WithFormFieldName;
import com.commercetools.sunrise.search.facetedsearch.mappers.FacetMapperSettings;

import javax.annotation.Nullable;
import java.util.Locale;

import static com.commercetools.sunrise.search.SearchUtils.localizeExpression;

public interface FacetedSearchFormSettings extends WithFormFieldName {

    /**
     * Gets the label displayed in the facet.
     * @return the label displayed in this facet
     */
    String getLabel();

    /**
     * Gets the facet expression associated, representing just the attribute path.
     * The expression might contain {@code {{locale}}}, which should be replaced with the current locale before using it.
     * @return the facet expression
     */
    String getExpression();

    /**
     * Gets the localized expression associated, representing just the attribute path.
     * This expression is ready to be sent to the CTP platform.
     * @param locale the current user's locale
     * @return the localized facet expression
     */
    default String getLocalizedExpression(final Locale locale) {
        return localizeExpression(getExpression(), locale);
    }

    /**
     * Gets the type of this facet.
     * @return the type of this facet
     */
    FacetUIType getType();

    /**
     * Gets the threshold indicating the minimum amount of options allowed to be displayed in the facet.
     * @return the threshold for the amount of options that can be displayed, or absent if it has no threshold
     */
    @Nullable
    Long getThreshold();

    /**
     * Gets the limit for the maximum amount of options allowed to be displayed in the facet.
     * @return the limit for the amount of options that can be displayed, or absent if it has no limit
     */
    @Nullable
    Long getLimit();

    /**
     * Whether the facet count should be hidden or not.
     * @return true if the count should be hidden, false otherwise
     */
    boolean isCountDisplayed();

    /**
     * Defines whether multiple options can be selected or only one.
     * @return true if multiple values can be selected, false otherwise
     */
    boolean isMultiSelect();

    /**
     * Defines whether the results should match all selected values in the facet (AND operator effect)
     * or just at least one selected value (OR operator effect)
     * @return true if results should match all selected values, false otherwise
     */
    boolean isMatchingAll();

    /**
     * Gets the mapper type for this facet.
     * @return the facet option mapper, or absent if there is no mapper
     */
    @Nullable
    FacetMapperSettings getMapper();
}