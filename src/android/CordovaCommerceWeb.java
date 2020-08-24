package org.apache.cordova.plugin.mastercard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mastercard.commerce.CardType;
import com.mastercard.commerce.CheckoutButton;
import com.mastercard.commerce.CheckoutCallback;
import com.mastercard.commerce.CheckoutRequest;
import com.mastercard.commerce.CommerceConfig;
import com.mastercard.commerce.CommerceWebSdk;
import com.mastercard.commerce.CryptoOptions;
import com.mastercard.commerce.Mastercard;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/* Copyright © 2020 Mastercard. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 =============================================================================*/


/**
 * This class is  used to call CommerceWeb SDK functionality from JavaScript.
 */
public class CordovaCommerceWeb extends CordovaPlugin {

  private static final String TAG = CordovaCommerceWeb.class.getSimpleName();
  private static final String ACTION_INITIALIZE_SDK = "initializeSdk";
  private static final String ACTION_CHECKOUT = "checkout";
  private static final String ACTION_GET_CHECKOUT_BUTTON = "getCheckoutButton";
  private static final String CARD_TYPE_MASTER = "master";
  private static final String CARD_TYPE_VISA = "visa";
  private static final String CARD_TYPE_AMEX = "amex";
  private static final String CARD_TYPE_JCB = "jcb";
  private static final String CARD_TYPE_DISCOVER = "discover";
  private static final String CARD_TYPE_MAESTRO = "maestro";
  private static final String CARD_TYPE_DINER = "diner";
  private static final String CRYPTO_OPTIONS_TYPE_ICC = "ICC";
  private static final String CRYPTO_OPTIONS_TYPE_UCAF = "UCAF";
  private static final int IMAGE_QUALITY_HIGH = 100;
  private static final String TRANSACTION_ID = "TransactionId";

  private CommerceWebSdk commerceWebSdk;
  private Gson gson = new GsonBuilder().create();
  private CallbackContext callbackContext = null;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
      super.initialize(cordova, webView);
      Log.d(TAG, "Mastercard CordovaCommerceWeb plugin initialized");
  }

  @Override
  public void onNewIntent(Intent intent) {
    //handle call back transaction ID back to JS
    if(intent.getExtras()!=null){
      PluginResult result = null;
      if(intent.getExtras().getString(TRANSACTION_ID) != null){
        String transcationID = intent.getExtras().getString(TRANSACTION_ID);
        result = new PluginResult(PluginResult.Status.OK,transcationID);
      }else{
        result = new PluginResult(PluginResult.Status.ERROR, "Expected transaction ID non-empty.");
      }
      this.callbackContext.sendPluginResult(result);
    }
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.callbackContext = callbackContext;
    if (action.equalsIgnoreCase(ACTION_INITIALIZE_SDK)) {
        this.initializeSdk(args.getString(0),callbackContext);
      return true;
    } else if (action.equalsIgnoreCase(ACTION_CHECKOUT)) {
        this.checkout(args.getString(0),callbackContext);
      return true;
    } else if (action.equalsIgnoreCase(ACTION_GET_CHECKOUT_BUTTON)) {
        this.getCheckoutButton(args.getString(0), callbackContext);
      return true;
    }

    return false;
  }

  private void initializeSdk(String configurationString,final CallbackContext callbackContext) {
    PluginResult result = null;
    if (configurationString != null && configurationString.length() > 0) {
        ConfigurationModel configModel = gson.fromJson(configurationString, ConfigurationModel.class);
        CommerceConfig config =
            new CommerceConfig(configModel.getLocale(), configModel.getCheckoutId(), configModel.getCheckoutUrl(), getAllowedCardTypes(configModel));

        commerceWebSdk = CommerceWebSdk.getInstance();
        commerceWebSdk.initialize(cordova.getActivity().getApplicationContext(), config);
        result = new PluginResult(PluginResult.Status.OK, configurationString);
        this.callbackContext.sendPluginResult(result);
    } else {
        result = new PluginResult(PluginResult.Status.ERROR, "Expected initializeSdk non-empty string argument.");
        this.callbackContext.sendPluginResult(result);
    }
  }

  private void checkout(String checkoutRequestString,final CallbackContext callbackContext) {
    PluginResult result = null;
    if (checkoutRequestString != null && checkoutRequestString.length() > 0) {
        CheckoutRequest request = buildCheckoutRequest(checkoutRequestString);
        commerceWebSdk.checkout(request);
    } else {
      result = new PluginResult(PluginResult.Status.ERROR, "Expected checkout non-empty string argument.");
      this.callbackContext.sendPluginResult(result);
    }
  }

  private void getCheckoutButton(String checkoutRequestString, final CallbackContext callbackContext) {
    if (checkoutRequestString != null && checkoutRequestString.length() > 0) {
        CheckoutButton checkoutButton = commerceWebSdk.getCheckoutButton(getCheckoutCallback(checkoutRequestString));
        String checkoutButtonImage = getBase64ImageString(checkoutButton.getDrawable(), IMAGE_QUALITY_HIGH);
        this.callbackContext.success(checkoutButtonImage);
    } else {
        this.callbackContext.error("Expected getCheckoutButton non-empty string argument.");
    }
  }


  private CheckoutCallback getCheckoutCallback(final String checkoutRequestString) {

    return listener -> {
      CheckoutRequest request = buildCheckoutRequest(checkoutRequestString);
      listener.setRequest(request);
    };
  }

  private CheckoutRequest buildCheckoutRequest(String checkoutRequestString) {
    CheckoutRequestModel checkoutRequestModel = gson.fromJson(checkoutRequestString, CheckoutRequestModel.class);

    return new CheckoutRequest.Builder().amount(checkoutRequestModel.getAmount())
            .cartId(checkoutRequestModel.getCartId())
            .currency(checkoutRequestModel.getCurrency())
            .callbackUrl(checkoutRequestModel.getCallbackUrl())
            .cryptoOptions(getCryptoOptions(checkoutRequestModel))
            .cvc2Support(checkoutRequestModel.isCvc2Support())
            .shippingLocationProfile(checkoutRequestModel.getShippingLocationProfile())
            .suppress3ds(checkoutRequestModel.isSuppress3Ds())
            .suppressShippingAddress(checkoutRequestModel.isSuppressShippingAddress())
            .unpredictableNumber(checkoutRequestModel.getUnpredictableNumber())
            .validityPeriodMinutes(checkoutRequestModel.getValidityPeriodMinutes())
            .build();
  }

  private Set<CardType> getAllowedCardTypes(ConfigurationModel configurationModel) {
    Set<CardType> allowedCardTypeSet = new HashSet<>();

    for (String cardType : configurationModel.getAllowedCardTypes()) {
      if (cardType.equalsIgnoreCase(CARD_TYPE_MASTER)) {
        allowedCardTypeSet.add(CardType.MASTER);
      } else if (cardType.equalsIgnoreCase(CARD_TYPE_VISA)) {
        allowedCardTypeSet.add(CardType.VISA);
      } else if (cardType.equalsIgnoreCase(CARD_TYPE_AMEX)) {
        allowedCardTypeSet.add(CardType.AMEX);
      } else if (cardType.equalsIgnoreCase(CARD_TYPE_JCB)) {
        allowedCardTypeSet.add(CardType.JCB);
      } else if (cardType.equalsIgnoreCase(CARD_TYPE_DISCOVER)) {
        allowedCardTypeSet.add(CardType.DISCOVER);
      } else if (cardType.equalsIgnoreCase(CARD_TYPE_MAESTRO)) {
        allowedCardTypeSet.add(CardType.MAESTRO);
      } else if (cardType.equalsIgnoreCase(CARD_TYPE_DINER)) {
        allowedCardTypeSet.add(CardType.DINERS);
      }
    }

    return allowedCardTypeSet;
  }

  private Set<CryptoOptions> getCryptoOptions(CheckoutRequestModel checkoutRequestModel) {
    Set<Mastercard.MastercardFormat> mastercardFormatSet = new HashSet<>();

    for (String cryptoOptionsType : checkoutRequestModel.getCryptoOptions()) {
      if (cryptoOptionsType.equalsIgnoreCase(CRYPTO_OPTIONS_TYPE_ICC)) {
        mastercardFormatSet.add(Mastercard.MastercardFormat.ICC);
      } else if (cryptoOptionsType.equalsIgnoreCase(CRYPTO_OPTIONS_TYPE_UCAF)) {
        mastercardFormatSet.add(Mastercard.MastercardFormat.UCAF);
      }
    }

    CryptoOptions mastercard = new Mastercard(mastercardFormatSet);
    Set<CryptoOptions> cryptoOptionsSet = new HashSet<>();
    cryptoOptionsSet.add(mastercard);

    return cryptoOptionsSet;
  }

  private String getBase64ImageString(Drawable drawable, int imageQuality) {
    Bitmap anImage = ((BitmapDrawable) drawable).getBitmap();

    return encodeToBase64(anImage, Bitmap.CompressFormat.PNG, imageQuality);
  }

  private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
    ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
    image.compress(compressFormat, quality, byteArrayOS);

    return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
  }

}