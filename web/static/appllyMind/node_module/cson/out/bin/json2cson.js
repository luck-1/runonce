// Generated by CoffeeScript 1.8.0
(function() {
  var CSON, argv, filePath, fs, path, result;

  fs = require('fs');

  path = require('path');

  CSON = require(path.join(__dirname, '..', 'lib', 'cson'));

  argv = process.argv;

  if (argv.length !== 3) {
    console.log('usage: json2cson filePath');
    process.exit(1);
  }

  filePath = argv[2];

  result = CSON.stringifySync(CSON.parseFileSync(filePath));

  console.log(result);

}).call(this);
