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

var exec = require('cordova/exec');

exports.initializeSdk = function(configuration, success, error) {
    exec(success, error, 'CordovaCommerceWeb', 'initializeSdk', [configuration])
};

exports.checkout = function(checkoutRequest, success, error) {
    exec(success, error, 'CordovaCommerceWeb', 'checkout', [checkoutRequest])
};

exports.getCheckoutButton = function(checkoutRequest, checkoutButtonSuccessCallback, success, error) {
   exec(function callback(base64Image) {
   var checkoutButtonURL = "data:image/png;charset=utf-8;base64, " + base64Image;
   checkoutButtonSuccessCallback(checkoutButtonURL); }, error, 'CordovaCommerceWeb', 'getCheckoutButton', [checkoutRequest])
};
