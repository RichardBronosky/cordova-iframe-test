cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/com.danielcwilson.plugins.iframer/www/Iframer.js",
        "id": "com.danielcwilson.plugins.iframer.Iframer",
        "clobbers": [
            "iframer"
        ]
    },
    {
        "file": "plugins/com.postmedia.DFPPlugin/www/DFPPlugin.js",
        "id": "com.postmedia.DFPPlugin.DFPPlugin",
        "clobbers": [
            "window.DFPPlugin"
        ]
    },
    {
        "file": "plugins/org.apache.cordova.inappbrowser/www/inappbrowser.js",
        "id": "org.apache.cordova.inappbrowser.inappbrowser",
        "clobbers": [
            "window.open"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "com.danielcwilson.plugins.iframer": "0.1.0",
    "com.postmedia.DFPPlugin": "0.0.4",
    "org.apache.cordova.inappbrowser": "0.5.4"
}
// BOTTOM OF METADATA
});