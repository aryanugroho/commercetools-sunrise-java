package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils;
import com.commercetools.sunrise.search.facetedsearch.old.FacetedSearchSelector;
import com.commercetools.sunrise.search.facetedsearch.old.SelectFacetedSearchConfig;
import com.commercetools.sunrise.search.facetedsearch.old.SelectFacetedSearchSelectorFactory;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.AlphabeticallySortedTermFacetMapper;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.SelectFacet;
import io.sphere.sdk.facets.SelectFacetBuilder;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.FilterExpression;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import play.mvc.Http;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class SelectFacetedSearchSelectorFactoryTest {

    private static final String KEY = "key";
    private static final String LABEL = "Some Facet";

    @Test
    public void initializesFacet() throws Exception {
        final SelectFacetedSearchConfig config = SelectFacetedSearchConfig.of(facetBuilder("foo.bar").mapper(AlphabeticallySortedTermFacetMapper.of()), 1);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, facetSelector -> {
            final Facet<ProductProjection> facet = facetSelector.getFacet(searchResult());
            assertThat(facet).as("class").isInstanceOf(SelectFacet.class);
            final SelectFacet<ProductProjection> selectFacet = (SelectFacet<ProductProjection>) facet;
            assertThat(selectFacet.getType()).as("type").isEqualTo(SunriseFacetType.LIST);
            assertThat(selectFacet.getKey()).as("key").isEqualTo(KEY);
            assertThat(selectFacet.getLabel()).as("label").isEqualTo(LABEL);
            assertThat(selectFacet.isCountHidden()).as("count hidden").isTrue();
            assertThat(selectFacet.isMatchingAll()).as("matching all").isTrue();
            assertThat(selectFacet.isMultiSelect()).as("multi select").isFalse();
            assertThat(selectFacet.getLimit()).as("limit").isEqualTo(3L);
            assertThat(selectFacet.getThreshold()).as("threshold").isEqualTo(2L);
            assertThat(selectFacet.getMapper()).as("mapper").isInstanceOf(AlphabeticallySortedTermFacetMapper.class);
            assertThat(selectFacet.getSelectedValues()).as("selected values").containsExactlyElementsOf(selectedValues);
        });
    }

    @Test
    public void getsFacetedSearchExprWithSelectedValueMatchingAll() throws Exception {
        final SelectFacetedSearchConfig config = SelectFacetedSearchConfig.of(facetBuilder("attr"), 1);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, facetSelector -> {
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("attr");
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().filterExpressions()).extracting(FilterExpression::expression).containsOnly("attr:\"foo\"", "attr:\"bar\"");
        });
    }

    @Test
    public void getsFacetedSearchExprWithSelectedValueMatchingAny() throws Exception {
        final SelectFacetedSearchConfig config = SelectFacetedSearchConfig.of(facetBuilder("attr").matchingAll(false), 1);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, facetSelector -> {
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("attr");
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().filterExpressions()).extracting(FilterExpression::expression).containsOnly("attr:\"foo\",\"bar\"");
        });
    }

    @Test
    public void getsFacetedSearchExprWithLocale() throws Exception {
        final SelectFacetedSearchConfig config = SelectFacetedSearchConfig.of(facetBuilder("some.{{locale}}.foo.{{locale}}.bar"), 1);
        final List<String> selectedValues = emptyList();
        test(config, selectedValues,  facetSelector -> {
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("some.en.foo.en.bar");
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().filterExpressions()).isEmpty();
        });
    }

    private SelectFacetBuilder<ProductProjection> facetBuilder(final String attrPath) {
        return SelectFacetBuilder.of(KEY, TermFacetedSearchSearchModel.<ProductProjection>of(attrPath))
                .label(LABEL)
                .type(SunriseFacetType.LIST)
                .countHidden(true)
                .matchingAll(true)
                .multiSelect(false)
                .limit(3L)
                .threshold(2L);
    }

    private void test(final SelectFacetedSearchConfig config, final List<String> selectedValues, final Consumer<FacetedSearchSelector> test) {
        final String url = QueryStringUtils.buildUri("path", singletonMap(config.getFacetBuilder().getKey(), selectedValues));
        final Http.Request request = new Http.RequestBuilder().uri(url).build();
        final SelectFacetedSearchSelectorFactory factory = new SelectFacetedSearchSelectorFactory(Locale.ENGLISH, CategoryTree.of(emptyList()), request);
        test.accept(factory.create(config, emptyList()));
    }

    private static PagedSearchResult<ProductProjection> searchResult() {
        return readObjectFromResource("search/pagedSearchResult.json", ProductProjectionSearch.resultTypeReference());
    }
}
