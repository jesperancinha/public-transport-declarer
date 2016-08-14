/// <reference path="../../typings/browser.d.ts" />
import { Component }  from '@angular/core';
import { ROUTER_DIRECTIVES } from '@angular/router';
import { HTTP_PROVIDERS } from '@angular/http';
import { APP_ROUTES_PROVIDER } from './app.routes';

let template = require('./app.html');

@Component({
  selector: 'bisca-je-content',
  template: template,
})
export class App {
  constructor() {
  }
}

