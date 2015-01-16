var exec = require('cordova/exec'),
    cordova = require('cordova');

function Iframer() {

}

Iframer.prototype.click = function(success, error) {
  exec(success || (function() { }), error || (function() { }), "Iframer", "click", []);
};

module.exports = new Iframer();
