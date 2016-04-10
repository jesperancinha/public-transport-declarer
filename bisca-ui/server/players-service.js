var PROPERTIES = require('./mock-players').data,
    storage = [];

function findAll(req, res, next) {
    return res.json(PROPERTIES);

};

exports.findAll = findAll;
