var readline = require('readline');

var ipToApp = {};
var stats = {};

var updateStats = function(data) {
  var type = data._type;
  if(!isNullOrUndefined(type)) {
    var app = data._meta.app;
    if(isNullOrUndefined(stats[app])) {
      stats[app] = {
        success : { count : 0 },
        error : { count : 0 },
        receivers : {}
      };
    }
    stats[app][type].count++;

    if(type == "success") {
      ipToApp[app] = data.requestSenderProperties.la_senderIps[0];

      if(isNullOrUndefined(stats[app].receivers[data.responseSenderProperties.la_senderIps[0]])) {
        stats[app].receivers[data.responseSenderProperties.la_senderIps[0]] = 1;
      } else {
        stats[app].receivers[data.responseSenderProperties.la_senderIps[0]]++;
      }
    }
  }
};

var isNullOrUndefined = function(obj) {
  return obj == null || obj == undefined;
};

var processLine = function(line) {
  stat = convertLineToStat(line);
  updateStats(stat);
};

var convertLineToMeta = function(line) {
  var match = /^([^\s]+)\s([^\s]+)/.exec(line);
  
  if(!isNullOrUndefined(match) && match.length > 1) {
    return {
      timestamp : match[1],
      app : match[2]
    };
  }
  
  return {};
};

var convertLineToStat = function(line) {
  var statsSuccessStr = "STAT SUCCESS ";
  var statsErrorStr = "STAT ERROR ";
  var pos = -1;

  var data = {};
  if((pos = line.indexOf(statsSuccessStr)) > -1) {
    data = stripUnnecessarySenderIps(convertVCAP_APPLICATIONStrToObject(JSON.parse(line.substr(pos + statsSuccessStr.length))));
    data._type = "success";
    data._meta = convertLineToMeta(line);
  } else if((pos = line.indexOf(statsErrorStr)) > -1) {
    data = JSON.parse(line.substr(pos + statsErrorStr.length));
    data._type = "error"; 
    data._meta = convertLineToMeta(line);
  } 
  return data;
};

var stripUnnecessarySenderIps = function(data) {
  var strip = function(list) {
    var newList = [];
    for(i in list) {
      var ip = list[i];
      if(!isNullOrUndefined(ip) && ip.length<=15 && ip != "127.0.0.1") {
        newList.push(ip);
      }
    }
    return newList;
  };

  if(!isNullOrUndefined(data)) {
    if(!isNullOrUndefined(data.requestSenderProperties) && !isNullOrUndefined(data.requestSenderProperties.la_senderIps)) {
      data.requestSenderProperties.la_senderIps = strip(data.requestSenderProperties.la_senderIps);
    }

    if(!isNullOrUndefined(data.responseSenderProperties) && !isNullOrUndefined(data.responseSenderProperties.la_senderIps)) {
      data.responseSenderProperties.la_senderIps = strip(data.responseSenderProperties.la_senderIps);
    }

    if(!isNullOrUndefined(data.senderProperties) && !isNullOrUndefined(data.senderProperties.la_senderIps)) {
      data.senderProperties.la_senderIps = strip(data.senderProperties.la_senderIps);
    }
  }
  return data;
};

var convertVCAP_APPLICATIONStrToObject = function(data) {
  if(!isNullOrUndefined(data)) {
    if(!isNullOrUndefined(!data.requestSenderProperties) && !isNullOrUndefined(data.requestSenderProperties.VCAP_APPLICATION)) {
      data.requestSenderProperties.VCAP_APPLICATION = JSON.parse(data.requestSenderProperties.VCAP_APPLICATION);
    } 
    if(!isNullOrUndefined(data.responseSenderProperties) && !isNullOrUndefined(data.responseSenderProperties.VCAP_APPLICATION)) {
      data.responseSenderProperties.VCAP_APPLICATION = JSON.parse(data.responseSenderProperties.VCAP_APPLICATION);
    }
  }
  return data;
};

readline.createInterface({
  input: process.stdin,
  output: process.stdout
}).on('line', function(line) {
  processLine(line);
}).on('close', function() {
  console.log("--------------------------");
  console.log(JSON.stringify(ipToApp));
  console.log("--------------------------");
  console.log(JSON.stringify(stats));
});
