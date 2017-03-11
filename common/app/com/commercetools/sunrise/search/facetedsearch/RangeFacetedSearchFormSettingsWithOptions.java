package com.commercetools.sunrise.search.facetedsearch;

import java.util.List;

public interface RangeFacetedSearchFormSettingsWithOptions extends FacetedSearchFormSettingsWithOptions<RangeFacetedSearchFormOption> {

    static RangeFacetedSearchFormSettingsWithOptions of(final FacetedSearchFormSettings settings, final List<RangeFacetedSearchFormOption> options) {
        return new RangeFacetedSearchFormSettingsWithOptionsImpl(settings, options);
    }
}