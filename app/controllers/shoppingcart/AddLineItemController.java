package controllers.shoppingcart;

import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.cart.addlineitem.AddLineItemControllerAction;
import com.commercetools.sunrise.framework.cart.addlineitem.AddLineItemFormData;
import com.commercetools.sunrise.framework.cart.addlineitem.CartCreator;
import com.commercetools.sunrise.framework.cart.addlineitem.SunriseAddLineItemController;
import com.commercetools.sunrise.framework.cart.cartdetail.viewmodels.CartDetailPageContentFactory;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.PageHeaderControllerComponentSupplier;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class
})
public final class AddLineItemController extends SunriseAddLineItemController {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public AddLineItemController(final TemplateRenderer templateRenderer,
                                 final FormFactory formFactory,
                                 final AddLineItemFormData formData,
                                 final CartFinder cartFinder,
                                 final CartCreator cartCreator,
                                 final AddLineItemControllerAction controllerAction,
                                 final CartDetailPageContentFactory pageContentFactory,
                                 final CartReverseRouter cartReverseRouter) {
        super(templateRenderer, formFactory, formData, cartFinder, cartCreator, controllerAction, pageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final AddLineItemFormData formData) {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }
}
