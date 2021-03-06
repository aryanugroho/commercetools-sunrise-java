package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionCheckoutReverseRouter extends ReflectionReverseRouterBase implements CheckoutReverseRouter {

    private final ReverseCaller checkoutAddressesPageCaller;
    private final ReverseCaller checkoutAddressesProcessFormCaller;
    private final ReverseCaller checkoutShippingPageCaller;
    private final ReverseCaller checkoutShippingProcessFormCaller;
    private final ReverseCaller checkoutPaymentPageCaller;
    private final ReverseCaller checkoutPaymentProcessFormCaller;
    private final ReverseCaller checkoutConfirmationPageCaller;
    private final ReverseCaller checkoutConfirmationProcessFormCaller;
    private final ReverseCaller checkoutThankYouPageCaller;

    @Inject
    private ReflectionCheckoutReverseRouter(final ParsedRoutes parsedRoutes) {
        checkoutAddressesPageCaller = getCallerForRoute(parsedRoutes, "checkoutAddressesPageCall");
        checkoutAddressesProcessFormCaller = getCallerForRoute(parsedRoutes, "checkoutAddressesProcessFormCall");
        checkoutShippingPageCaller = getCallerForRoute(parsedRoutes, "checkoutShippingPageCall");
        checkoutShippingProcessFormCaller = getCallerForRoute(parsedRoutes, "checkoutShippingProcessFormCall");
        checkoutPaymentPageCaller = getCallerForRoute(parsedRoutes, "checkoutPaymentPageCall");
        checkoutPaymentProcessFormCaller = getCallerForRoute(parsedRoutes, "checkoutPaymentProcessFormCall");
        checkoutConfirmationPageCaller = getCallerForRoute(parsedRoutes, "checkoutConfirmationPageCall");
        checkoutConfirmationProcessFormCaller = getCallerForRoute(parsedRoutes, "checkoutConfirmationProcessFormCall");
        checkoutThankYouPageCaller = getCallerForRoute(parsedRoutes, "checkoutThankYouPageCall");
    }

    @Override
    public Call checkoutAddressesPageCall(final String languageTag) {
        return checkoutAddressesPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutAddressesProcessFormCall(final String languageTag) {
        return checkoutAddressesProcessFormCaller.call(languageTag);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return checkoutShippingPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutShippingProcessFormCall(final String languageTag) {
        return checkoutShippingProcessFormCaller.call(languageTag);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return checkoutPaymentPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutPaymentProcessFormCall(final String languageTag) {
        return checkoutPaymentProcessFormCaller.call(languageTag);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return checkoutConfirmationPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutConfirmationProcessFormCall(final String languageTag) {
        return checkoutConfirmationProcessFormCaller.call(languageTag);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return checkoutThankYouPageCaller.call(languageTag);
    }
}
