'use strict';

var toArray = require('../../../lib/Array/from')

  , f;

f = function (a, b) {
	return toArray(arguments);
};

module.exports = function (t, a) {
	a.deep(t.call(f)(1, 2, 3), [2, 1, 3]);
};
