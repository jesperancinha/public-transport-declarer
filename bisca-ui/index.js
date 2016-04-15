"use strict";
var browser_1 = require('angular2/platform/browser');
var core_1 = require('angular2/core');
var common_1 = require('angular2/common');
var router_1 = require('angular2/router');
var http_1 = require('angular2/http');
var angular2_jwt_1 = require('angular2-jwt');
var app_1 = require('./app/app');
browser_1.bootstrap(app_1.App, [
    common_1.FORM_PROVIDERS,
    router_1.ROUTER_PROVIDERS,
    http_1.HTTP_PROVIDERS,
    core_1.provide(angular2_jwt_1.AuthHttp, {
        useFactory: function (http) {
            return new angular2_jwt_1.AuthHttp(new angular2_jwt_1.AuthConfig({
                tokenName: 'jwt'
            }), http);
        },
        deps: [http_1.Http]
    })
]);
//# sourceMappingURL=index.js.map