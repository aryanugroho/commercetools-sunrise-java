package com.commercetools.sunrise.common.localization.changecountry;

import com.commercetools.sunrise.framework.controllers.SunriseFormController;
import com.commercetools.sunrise.framework.controllers.WithFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.common.LocalizationReverseRouter;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseChangeCountryController extends SunriseFormController implements WithFormFlow<ChangeCountryFormData, Void, Void> {

    private final ChangeCountryFormData formData;
    private final ChangeCountryControllerAction controllerAction;

    protected SunriseChangeCountryController(final FormFactory formFactory, final ChangeCountryFormData formData,
                                             final ChangeCountryControllerAction controllerAction) {
        super(formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public Class<? extends ChangeCountryFormData> getFormDataClass() {
        return formData.getClass();
    }

    @RunRequestStartedHook
    @SunriseRoute(LocalizationReverseRouter.CHANGE_COUNTRY_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final ChangeCountryFormData formData) {
        controllerAction.accept(formData);
        return completedFuture(null);
    }
}
