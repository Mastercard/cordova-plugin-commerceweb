package org.apache.cordova.plugin.mastercard;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Locale;

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
 * Model class to build CommerceConfig object passed during initialization of CommerceWebSDK>
 */
public class ConfigurationModel {

  @SerializedName("checkoutId")
  private String checkoutId;
  @SerializedName("checkoutUrl")
  private String checkoutUrl;
  @SerializedName("locale")
  private Locale locale;
  @SerializedName("allowedCardTypes")
  private ArrayList<String> allowedCardTypes;

  public String getCheckoutId() {
    return checkoutId;
  }

  public void setCheckoutId(String checkoutId) {
    this.checkoutId = checkoutId;
  }

  public String getCheckoutUrl() {
    return checkoutUrl;
  }

  public void setCheckoutUrl(String checkoutUrl) {
    this.checkoutUrl = checkoutUrl;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public ArrayList<String> getAllowedCardTypes() {
    return allowedCardTypes;
  }

  public void setAllowedCardTypes(ArrayList<String> allowedCardTypes) {
    this.allowedCardTypes = allowedCardTypes;
  }
}
