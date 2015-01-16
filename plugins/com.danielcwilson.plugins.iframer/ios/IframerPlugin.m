//IframerPlugin.m
//Created by Daniel Wilson 2015-01-15

#import "IframerPlugin.h"

@implementation IframerPlugin

- (void) pluginInitialize
{

}

- (void) click: (CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;

    NSLog(@"***** Iframe Clicked!");


    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


@end
