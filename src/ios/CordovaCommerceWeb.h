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

#import <Cordova/CDVPlugin.h>
#import "CordovaCommerceWeb.h"
#import <MCSCommerceWeb/MCSCommerceWeb.h>
#import <MCSCommerceWeb/MCSConfiguration.h>

@interface CordovaCommerceWeb : CDVPlugin <MCSCheckoutDelegate>

/// Callback ID to return to cordova app with plugin result
@property (nonatomic, strong) NSString* callbackId;

/// Initialize SDK
/// @param command config
-(void)initializeSdk:(CDVInvokedUrlCommand *)command;

/// Checkout SDK
/// @param command checkoutRequest
-(void)checkout:(CDVInvokedUrlCommand *)command;

/// Get Checkout Button
/// @param command 
-(void)getCheckoutButton:(CDVInvokedUrlCommand *)command;

@end
