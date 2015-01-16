//IframerPlugin.h
//Created by Daniel Wilson 2015-01-15

#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>

@interface IframerPlugin : CDVPlugin {

}

- (void) click: (CDVInvokedUrlCommand*)command;

@end
