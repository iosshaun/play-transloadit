var crypto = require('crypto');

utcDateString = function(time) {
  function pad(val, len) {
    val = String(val);
    len = len || 2;
    while (val.length < len) val = "0" + val;
    return val;
  }

  var now = new Date();
  now.setTime(time);

  var utc = new Date(
    Date.UTC(
      now.getFullYear(),
      now.getMonth(),
      now.getDate(),
      now.getHours(),
      now.getMinutes(),
      now.getSeconds()
    )
  );

  var cDate  = utc.getDate();
  var cMonth = utc.getMonth();
  var cYear  = utc.getFullYear();
  var cHour  = utc.getHours();
  var cMin   = utc.getMinutes();
  var cSec   = utc.getSeconds();

  var result = cYear + '/' + pad((cMonth + 1)) + '/' + pad(cDate);
  result += ' ' + cHour + ':' + pad(cMin) + ':' + pad(cSec) + '+00:00';

  return result;
};

// 3 hours from now (this must be milliseconds)
var expiresIn  = 3 * 60 * 60 * 1000;
var expires    = utcDateString((+new Date()) + expiresIn);

// Using very simple values we can easily compare the output of this and the Java impl.
var authKey    = 'KEY';
var authSecret = 'SECRET';
var params = "PARAMS";

var paramsString = JSON.stringify(params);

console.log(paramsString);

var signature = crypto
    .createHmac('sha1', authSecret)
    .update(paramsString)
    .digest('hex');

console.log(signature);
