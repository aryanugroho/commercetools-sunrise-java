package com.commercetools.sunrise.shoppingcart.cart.removelineitem;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.cart.SunriseCartManagementController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.CartDetailPageContent;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.client.ClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(SunriseRemoveLineItemHeroldComponent.class)
public abstract class SunriseRemoveLineItemController extends SunriseCartManagementController implements WithTemplateName, WithFormFlow<RemoveLineItemFormData, Cart, Cart> {

    private static final Logger logger = LoggerFactory.getLogger(SunriseRemoveLineItemController.class);

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("cart", "manage-cart", "remove-line-item-from-cart"));
        return frameworkTags;
    }

    @Override
    public Class<? extends RemoveLineItemFormData> getFormDataClass() {
        return DefaultRemoveLineItemFormData.class;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @SunriseRoute("processDeleteLineItemForm")
    public CompletionStage<Result> removeLineItem(final String languageTag) {
        return doRequest(() -> {
            logger.debug("process remove line item form in locale={}", languageTag);
            return doRequest(() -> findCart()
                    .thenComposeAsync(cartOptional -> cartOptional
                            .map(this::validateForm)
                            .orElseGet(this::redirectToCartDetail), HttpExecution.defaultContext()));
        });
    }

    @Override
    public CompletionStage<? extends Cart> doAction(final RemoveLineItemFormData formData, final Cart cart) {
        return removeLineItem(formData.getLineItemId(), cart);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends RemoveLineItemFormData> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException, logger);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final RemoveLineItemFormData formData, final Cart cart, final Cart updatedCart) {
        return redirectToCartDetail();
    }

    // TODO duplicated
    @Override
    public CompletionStage<Html> renderPage(final Form<? extends RemoveLineItemFormData> form, final Cart cart, @Nullable final Cart updatedCart) {
        final Cart cartToRender = Optional.ofNullable(updatedCart).orElse(cart);
        final CartDetailPageContent pageContent = injector().getInstance(CartDetailPageContentFactory.class).create(cartToRender);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void fillFormData(final RemoveLineItemFormData formData, final Cart context) {
        // Do nothing
    }

    protected CompletionStage<Cart> removeLineItem(final String lineItemId, final Cart cart) {
        final RemoveLineItem removeLineItem = RemoveLineItem.of(lineItemId);
        final CartUpdateCommand cmd = CartUpdateCommand.of(cart, removeLineItem);
        return executeCartUpdateCommandWithHooks(cmd);
    }
}
