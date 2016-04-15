"use strict";
var browser_1 = require('angular2/platform/browser');
var app_constants_1 = require('./app.constants');
var app_game_1 = require('./app.game');
var http_1 = require('angular2/http');
browser_1.bootstrap(app_game_1.PlayerStatusComponent, [http_1.HTTP_PROVIDERS, app_constants_1.Configuration]);
//# sourceMappingURL=game.js.map