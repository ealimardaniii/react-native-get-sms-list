#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(GetSmsList, NSObject)

RCT_EXTERN_METHOD(readSMS:(NSDictionary)options
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
