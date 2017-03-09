package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.framework.hooks.requests.ProductProjectionSearchHook;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.SortExpression;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils.findSelectedValueFromQueryString;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public final class SortSelectorControllerComponent extends Base implements ControllerComponent, PageDataReadyHook, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook {

    private final List<SortExpression<ProductProjection>> selectedSortExpressions;
    private final SortSelectorViewModelFactory sortSelectorViewModelFactory;

    @Nullable
    private PagedSearchResult<ProductProjection> pagedSearchResult;

    @Inject
    public SortSelectorControllerComponent(final Locale locale, final SortFormSettings settings, final Http.Request httpRequest,
                                           final SortSelectorViewModelFactory sortSelectorViewModelFactory) {
        this.selectedSortExpressions = findSelectedValueFromQueryString(settings, httpRequest)
                .map(option -> option.getLocalizedValue(locale))
                .orElse(emptyList());
        this.sortSelectorViewModelFactory = sortSelectorViewModelFactory;
    }

    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        if (!selectedSortExpressions.isEmpty()) {
            return search.plusSort(selectedSortExpressions);
        } else {
            return search;
        }
    }

    @Override
    public CompletionStage<?> onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        this.pagedSearchResult = pagedSearchResult;
        return completedFuture(null);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (pagedSearchResult != null && pageData.getContent() instanceof WithSortSelectorViewModel) {
            final WithSortSelectorViewModel content = (WithSortSelectorViewModel) pageData.getContent();
            content.setSortSelector(sortSelectorViewModelFactory.create(pagedSearchResult));
        }
    }
}
