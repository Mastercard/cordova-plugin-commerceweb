package org.apache.cordova.plugin.mastercard;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

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
 * Model class to build CheckoutRequest for CommerceSdk@
 */

public class CheckoutRequestModel {

  @SerializedName("amount")
  private double amount;

  @SerializedName("currency")
  private String currency;

  @SerializedName("callbackUrl")
  private String callbackUrl;

  @SerializedName("cryptoOptions")
  private ArrayList<String> cryptoOptions;

  @SerializedName("cvc2Support")
  private boolean cvc2Support;

  @SerializedName("shippingLocationProfile")
  private String shippingLocationProfile;

  @SerializedName("suppress3Ds")
  private boolean suppress3Ds;

  @SerializedName("suppressShippingAddress")
  private boolean suppressShippingAddress;

  @SerializedName("unpredictableNumber")
  private String unpredictableNumber;

  @SerializedName("validityPeriodMinutes")
  private Integer validityPeriodMinutes;

  @SerializedName("cartId")
  private String cartId;

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public boolean isSuppressShippingAddress() {
    return suppressShippingAddress;
  }

  public void setSuppressShippingAddress(boolean suppressShippingAddress) {
    this.suppressShippingAddress = suppressShippingAddress;
  }

  public ArrayList<String> getCryptoOptions() {
    return cryptoOptions;
  }

  public void setCryptoOptions(ArrayList<String> cryptoOptions) {
    this.cryptoOptions = cryptoOptions;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public boolean isCvc2Support() {
    return cvc2Support;
  }

  public void setCvc2Support(boolean cvc2Support) {
    this.cvc2Support = cvc2Support;
  }

  public String getShippingLocationProfile() {
    return shippingLocationProfile;
  }

  public void setShippingLocationProfile(String shippingLocationProfile) {
    this.shippingLocationProfile = shippingLocationProfile;
  }

  public boolean isSuppress3Ds() {
    return suppress3Ds;
  }

  public void setSuppress3Ds(boolean suppress3Ds) {
    this.suppress3Ds = suppress3Ds;
  }

  public String getUnpredictableNumber() {
    return unpredictableNumber;
  }

  public void setUnpredictableNumber(String unpredictableNumber) {
    this.unpredictableNumber = unpredictableNumber;
  }

  public Integer getValidityPeriodMinutes() {
    return validityPeriodMinutes;
  }

  public void setValidityPeriodMinutes(Integer validityPeriodMinutes) {
    this.validityPeriodMinutes = validityPeriodMinutes;
  }

  public String getCartId() {
    return cartId;
  }

  public void setCartId(String cartId) {
    this.cartId = cartId;
  }

}
