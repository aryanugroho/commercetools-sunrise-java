package com.commercetools.sunrise.search.facetedsearch.old;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.CategoryTreeTermFacetMapper;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.TermFacetsMapper2;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.model.FacetedSearchSearchModel;
import io.sphere.sdk.search.model.SearchModel;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils.findAllSelectedValuesFromQueryString;
import static com.commercetools.sunrise.search.SearchUtils.localizeExpression;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class SelectFacetedSearchSelectorFactory extends Base {

    private final Locale locale;
    private final CategoryTree categoryTree;
    private final Http.Request httpRequest;

    @Inject
    public SelectFacetedSearchSelectorFactory(final Locale locale, final CategoryTree categoryTree, final Http.Request httpRequest) {
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.httpRequest = httpRequest;
    }

    public FacetedSearchSelector create(final SelectFacetedSearchConfig facetConfig, final List<Category> selectedCategories) {
        final Facet<ProductProjection> facet = initializeFacet(facetConfig, selectedCategories);
        return FacetedSearchSelector.of(facet, facetConfig.getPosition());
    }

    private Facet<ProductProjection> initializeFacet(final SelectFacetedSearchConfig facetConfig, final List<Category> selectedCategories) {
        return facetConfig.getFacetBuilder()
                .mapper(configureMapper(facetConfig, selectedCategories))
                .facetedSearchSearchModel(configureSearchModel(facetConfig))
                .selectedValues(getSelectedValues(facetConfig, selectedCategories))
                .build();
    }

    @Nullable
    private TermFacetsMapper2 configureMapper(final SelectFacetedSearchConfig facetConfig, final List<Category> selectedCategories) {
        final TermFacetsMapper2 mapper = facetConfig.getFacetBuilder().getMapper();
        if (mapper != null && mapper instanceof CategoryTreeTermFacetMapper) {
            final CategoryTree categoriesInFacet = getCategoriesInFacet(selectedCategories);
            return ((CategoryTreeTermFacetMapper) mapper).withCategories(selectedCategories, categoriesInFacet, singletonList(locale));
        }
        return mapper;
    }

    private FacetedSearchSearchModel<ProductProjection> configureSearchModel(final SelectFacetedSearchConfig facetConfig) {
        final SearchModel<ProductProjection> searchModel = facetConfig.getFacetBuilder().getFacetedSearchSearchModel().getSearchModel();
        final String localizedAttrPath = localizeExpression(searchModel.attributePath(), locale);
        return TermFacetedSearchSearchModel.of(localizedAttrPath);
    }

    private List<String> getSelectedValues(final SelectFacetedSearchConfig facetConfig, final List<Category> selectedCategories) {
        final TermFacetsMapper2 mapper = facetConfig.getFacetBuilder().getMapper();
        if (mapper != null && mapper instanceof CategoryTreeTermFacetMapper) {
            return selectedCategories.stream()
                    .flatMap(category -> categoryTree.getSubtree(singletonList(category)).getAllAsFlatList().stream())
                    .map(Category::getId)
                    .distinct()
                    .collect(toList());
        } else {
            return findAllSelectedValuesFromQueryString(facetConfig.getFacetBuilder().getKey(), httpRequest);
        }
    }

    /**
     * Forms the category tree displayed in the category facet. That is, the tree containing:
     * - the category itself
     * - the ancestors tree
     * - the direct children categories
     * - the sibling categories
     * @param selectedCategories list of the selected categories
     * @return the new category tree containing only those categories displayed in the category facet
     */
    private CategoryTree getCategoriesInFacet(final List<Category> selectedCategories) {
        final List<Category> categoriesInFacet = selectedCategories.stream()
                .flatMap(category -> {
                    final List<Category> categories = new ArrayList<>();
                    categories.add(category);
                    categories.addAll(category.getAncestors().stream()
                            .map(c -> categoryTree.findById(c.getId()).orElse(null))
                            .filter(Objects::nonNull)
                            .collect(toList()));
                    categories.addAll(categoryTree.findChildren(category));
                    categories.addAll(categoryTree.findSiblings(singletonList(category)));
                    return categories.stream();
                })
                .distinct()
                .collect(toList());
        return CategoryTree.of(categoriesInFacet);
    }
}
