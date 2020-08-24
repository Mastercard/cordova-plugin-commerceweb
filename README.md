[![](https://img.shields.io/npm/v/mastercard-cordova-commerceweb.svg)](https://www.npmjs.com/package/mastercard-cordova-commerceweb)
[![](https://img.shields.io/badge/license-Apache%202.0-yellow.svg)](https://github.com/Mastercard/cordova-plugin-commerceweb/blob/master/LICENSE)

### Table of Contents

- [Overview](#overview)
- [Installation](#installation)
- [Configuration](#configuration)
- [Initialization](#initialization)
- [Checkout Request](#checkout-request)
- [Checkout](#checkout)
- [Checkout Button](#checkout-button)
- [Transaction Result](#transaction-result)
	- [Intent Scheme](#intent-scheme)

### <a name="overview">Overview</a>

This Cordova plugin enables to use  `commerce-web` SDK in a Cordova project. `commerce-web` is a lightweight library used to integrate Merchants with 
[**EMV Secure Remote Commerce**](https://www.emvco.com/emv-technologies/src/) 
and  Mastercard's web-based SRC-Initiator. `commerce-web` 
facilitates the initiation of the checkout experience and returns the 
transaction result to the Merchant after completion.

### <a name="installation">Installation</a>

To include `app-to-web` plugin in Cordova Merchant application, run following command

```groovy
$ cordova plugin add mastercard-cordova-commerceweb
```

### <a name="configuration">Configuration</a>

To initialize SDK, a configuration object needs to be provided. Configuration object requires the following parameters:

* `checkoutId`: The unique identifier assigned to the merchant during 
onboarding.
* `checkoutUrl`: The URL used to load the checkout experience. Note: 
when testing in the Sandbox environment, use 
**`https://sandbox.src.mastercard.com/srci/`**. For 
Production, use **`https://src.mastercard.com/srci/`**.
* `locale`: This is the locale in which the transaction is processing.
* `allowedCardTypes`: The payment networks supported by this merchant (e.g. master, visa, amex)
* `callbackScheme`: This must match the scheme component of the `callbackUrl` configured for this merchant. This value is required to redirect back to the merchant from the plugin. (`IOS only`)


Configuration object can be created as shown below:

```js
/**
 SDK Configuration Model Android
*/
var configurationAndroid = {
     "checkoutId": "{YOUR-CHECKOUT-ID}",
     "checkoutUrl": "https://sandbox.src.mastercard.com/srci/",
     "locale": "en_US",
     "allowedCardTypes": [
          "master",
          "visa",
          "amex"
         ]
}
```

```js
/**
 SDK Configuration Model IOS
*/
var configurationIOS = {
     "checkoutId": "{YOUR-CHECKOUT-ID}",
     "checkoutUrl": "https://sandbox.src.mastercard.com/srci/",
     "locale":"en_US",
     "callbackScheme":"{YOUR-SCHEME}",
     "allowedCardTypes": [
          "master",
          "visa",
          "amex"
         ]
}
```

### <a name="initialization">Initialization</a>

Before you can perform a checkout you need to initialize the SDK by calling `initializeSdk` method, can be called as shown below:

```js
 /**
 Initialization SDK call
*/
function initializeMethod() {
     cordova.plugins.mastercard.CordovaCommerceWeb.initializeSdk(configuration);
}
```

#### <a name="checkout-request">Checkout Request</a>

`checkoutRequest`: Data object with transaction-specific parameters 
needed to complete checkout. This request can also override existing 
merchant configurations.

Here are the required and optional fields:

| Parameter                | Type           | Required   | Description
|--------------------------|------------|:----------:|---------------------------------------------------------------------------------------------------------|
| amount                   | Double     | Yes        | The transaction total to be authorized
| cartId                   | String     | Yes        | Randomly generated UUID used as a transaction id
| currency                 | String     | Yes         | Currency of the transaction
| callbackUrl              | String     | No         | URL used to communicate back to the merchant application
| cryptoOptions            | Set\<CryptoOptions>     | No         | Cryptogram formats accepted by this merchant
| cvc2Support              | Boolean     | No         | Enable or disable support for CVC2 card security
| shippingLocationProfile  | String     | No         | Shipping locations available for this merchant
| suppress3Ds              | Boolean     | No         | Enable or disable 3DS verification
| suppressShippingAddress  | Boolean     | No         | Enable or disable shipping options. Typically for digital goods or services, this will be set to true
| unpredictableNumber      | String     | No         | For tokenized transactions, unpredictableNumber is required for cryptogram generation
| validityPeriodMinutes    | Integer     | No         | The expiration time of a generated cryptogram, in minutes

### Checkout Request Model Examples

```js
/**
  Checkout Request Model Android
 */
 var checkoutRequestAndroid = {
      "amount": 3.14,
      "currency": "USD",
      "cartId": create_UUID(),
      "suppressShippingAddress": "false",
      "cryptoOptions": [
            "ICC",
            "UCAF"
           ]
 }
 
 /**
  Checkout Request Model IOS
 */
 var checkoutRequestIOS = {
      "amount": 3.14,
      "currency": "USD",
      "cartId": create_UUID(),
      "callbackUrl":"fancyshop:// | {YOUR-SCHEME}",
      "suppressShippingAddress": "false",
      "cryptoOptions": [
            "ICC",
            "UCAF"
           ]
 }

/**
  Create UUID
 */
 function create_UUID(){
     var dt = new Date().getTime();
     var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
         var r = (dt + Math.random()*16)%16 | 0;
         dt = Math.floor(dt/16);
         return (c=='x' ? r :(r&0x3|0x8)).toString(16);
     });
     return uuid;
 }
```
### <a name="checkout">Checkout</a>

After SDK initialization you can call `checkout` with a `CheckoutRequest` model, plugin will return `Transaction ID` as a String inside the callback `transactionId` 

```js
/**
  Checkout SDK call
 */
 function checkoutMethod() {
      var devicePlatform = device.platform;
      if(devicePlatform === "iOS"){
          cordova.plugins.mastercard.CordovaCommerceWeb.checkout(checkoutRequestIOS,
           function(transactionId, error){
                if(error){
                     document.getElementById("checkoutResponse").value="Checkout SDK Failed!";
                }else{
                     //display transaction id
                     document.getElementById("checkoutResponse").value=transactionId;
                }
           });
      }else if(devicePlatform === "Android"){
          cordova.plugins.mastercard.CordovaCommerceWeb.checkout(checkoutRequestAndroid,
           function(transactionId, error){
                if(error){
                     document.getElementById("checkoutResponse").value="Checkout SDK Failed!";
                }else{
                     //display transaction id
                     document.getElementById("checkoutResponse").value=transactionId;
                }
          });
      }
  }
```


### <a name="checkout-button">Checkout Button Image</a>

Merchant can obtain our checkout button image calling the `getCheckoutButton` by passing a `checkoutRequest` model and listening for the callback `getCheckoutButtonSuccess`

```js
 cordova.plugins.mastercard.CordovaCommerceWeb.getCheckoutButton(checkoutRequest, getCheckoutButtonSuccess);
```

We return a Base64 encoded image to the merchant, it can be resize and display base on merchant needs

```js
//html image tag with style class
 <img id="checkoutButton" class="srcButton"></img>
```

```css
//CSS to display image
.srcButton{
    background-size: 100%;
    background-position: top center;
    background-repeat:no-repeat;
    width:235px;
    height:55px;
    border:none;
}
```

```js
/**
 Get Checkout Button Image call
*/
function getCheckoutButtonMethod() {
     var getCheckoutButtonSuccess = function(checkoutButtonImage) {
     //display image button to element on html
     document.getElementById("checkoutButton").src = checkoutButtonImage;
     }
     cordova.plugins.mastercard.CordovaCommerceWeb.getCheckoutButton(checkoutRequest, getCheckoutButtonSuccess);
     }
}
```

### <a name="android-specifict">Android Specific</a>

### <a name="transaction-result">Transaction Result</a>

The result of a transaction is returned to the application via an `Intent` containing the `transactionId`.

##### <a name="intent-scheme">Intent Scheme</a>

`callbackUrl` must be configured with an `Intent` URI. The transaction result is returned to the activity configured to receive the `Intent`. 
would define a URI similar to

`intent://{YOUR-SCHEME}/#Intent;package=com.package.{YOUR-SCHEME};scheme={YOUR-SCHEME};end`

In order to receive the result, the application must declare an `intent-filter` for the `Activity` receiving the `Intent`, the plugin already adds for you the intent filter, you only need to replace your scheme in the application `AndroidManifest.xml`


```xml
<data
                    android:host="commerce"
                    android:path="/"
                    android:scheme="{YOUR SCHEME}"/>
```
