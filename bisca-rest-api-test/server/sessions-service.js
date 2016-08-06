var PROPERTIES = require('./mock-session').data,
    storage = [];

function findAll(req, res, next) {
    return res.json(PROPERTIES);

};

exports.findAll = findAll;
