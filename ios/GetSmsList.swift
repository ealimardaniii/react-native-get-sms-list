@objc(GetSmsList)
class GetSmsList: NSObject {

  @objc(readSMS:withResolver:withRejecter:)
  func readSMS(a: NSDictionary, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
    resolve([])
  }
}
