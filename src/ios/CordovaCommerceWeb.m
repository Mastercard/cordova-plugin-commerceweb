
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


#import "CordovaCommerceWeb.h"

static NSString* toBase64(NSData* data) {
    SEL s1 = NSSelectorFromString(@"cdv_base64EncodedString");
    SEL s2 = NSSelectorFromString(@"base64EncodedString");
    SEL s3 = NSSelectorFromString(@"base64EncodedStringWithOptions:");

    if ([data respondsToSelector:s1]) {
        NSString* (*func)(id, SEL) = (void *)[data methodForSelector:s1];
        return func(data, s1);
    } else if ([data respondsToSelector:s2]) {
        NSString* (*func)(id, SEL) = (void *)[data methodForSelector:s2];
        return func(data, s2);
    } else if ([data respondsToSelector:s3]) {
        NSString* (*func)(id, SEL, NSUInteger) = (void *)[data methodForSelector:s3];
        return func(data, s3, 0);
    } else {
        return nil;
    }
}

@implementation CordovaCommerceWeb

/// Plugin initialization
-(void)pluginInitialize{
    NSLog(@"Mastercard CordovaCommerceWeb plugin initialized");
}

-(void)initializeSdk:(CDVInvokedUrlCommand *)command{
    self.callbackId = command.callbackId;
    CDVPluginResult* pluginResult = nil;
    NSDictionary* configDic = [command.arguments objectAtIndex:0];
    if (configDic != nil && configDic.count > 0) {
        NSLocale *locale = [[NSLocale alloc] initWithLocaleIdentifier:configDic[@"locale"]];
        NSString *checkoutId = configDic[@"checkoutId"];
        NSString *checkoutUrl = configDic[@"checkoutUrl"];
        NSString *callbackScheme = configDic[@"callbackScheme"];
        NSSet *allowedCardTypes = [NSSet setWithArray:configDic[@"allowedCardTypes"]];
        MCSConfiguration *config = [[MCSConfiguration alloc] initWithLocale:locale
                                                                 checkoutId:checkoutId
                                                                checkoutUrl:checkoutUrl
                                                             callbackScheme:callbackScheme
                                                           allowedCardTypes:allowedCardTypes
                                                   presentingViewController:nil];

        MCSCommerceWeb *commerceWeb = [MCSCommerceWeb sharedManager];
        [commerceWeb initWithConfiguration:config];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:configDic.description];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)checkout:(CDVInvokedUrlCommand *)command{
    self.callbackId = command.callbackId;
    NSDictionary* checkoutDic= [command.arguments objectAtIndex:0];
    MCSCheckoutRequest *checkoutRequest = [[MCSCheckoutRequest alloc] init];
    double amount = [[checkoutDic valueForKey:@"amount"] doubleValue];
    checkoutRequest.amount = [[NSDecimalNumber alloc] initWithDouble:amount];
    checkoutRequest.currency = checkoutDic[@"currency"];
    checkoutRequest.cartId = checkoutDic[@"cartId"];
    _Bool supress = checkoutDic[@"suppressShippingAddress"];
    checkoutRequest.suppressShippingAddress = !supress;
    
    checkoutRequest.callbackUrl = checkoutDic[@"calbackUrl"];
    checkoutRequest.unpredictableNumber = @"12345678";
    
    MCSCryptoOptions *cryptoOptionMaster = [[MCSCryptoOptions alloc] init];
    cryptoOptionMaster.cardType = MCSCardTypeMaster;
    cryptoOptionMaster.format = [NSSet setWithArray:checkoutDic[@"cryptoOptions"]];
    checkoutRequest.cryptoOptions = @[cryptoOptionMaster];
    MCSCommerceWeb *commerceWeb = [MCSCommerceWeb sharedManager];
    [commerceWeb setDelegate:self];
    [commerceWeb checkoutWithRequest:checkoutRequest];
}

-(void)getCheckoutButton:(CDVInvokedUrlCommand *)command{
    MCSCommerceWeb *commerceWeb = [MCSCommerceWeb sharedManager];
    MCSCheckoutButton *button = [commerceWeb checkoutButtonWithDelegate:self];
    //convert the image to NSData first
    NSData *nsdata = UIImagePNGRepresentation(button.imageView.image);
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:toBase64(nsdata)];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)checkoutRequest:(MCSCheckoutRequest *)request didCompleteWithStatus:(MCSCheckoutStatus)status forTransaction:(NSString *)transactionId{
    CDVPluginResult* pluginResult = nil;
    switch (status) {
        case MCSCheckoutStatusSuccess:
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:transactionId];
            break;
        case MCSCheckoutStatusCanceled:
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Can't complete Checkout"];
            break;
        default:
             pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Can't complete Checkout"];
            break;
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
}

- (void)checkoutRequestForTransaction:(nonnull void (^)(MCSCheckoutRequest * _Nonnull))handler {}

@end