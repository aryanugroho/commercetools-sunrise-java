package com.commercetools.sunrise.framework.reverserouters.productcatalog.product;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class ProductLinksControllerComponent extends AbstractLinksControllerComponent<ProductReverseRouter> {

    private final ProductReverseRouter reverseRouter;

    @Inject
    protected ProductLinksControllerComponent(final ProductReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final ProductReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final ProductReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.searchProcessCall(), "search");
    }
}