/// <reference path="../../typings/browser.d.ts" />

import {Component} from '@angular/core';

import {LoggedInRouterOutlet} from '../routing/logged.in.outlet';
import {Home} from '../home/home';
import {Login} from '../login/login';
import {Signup} from '../signup/signup';
import { provide } from '@angular/core';
import {bootstrap} from '@angular/platform-browser-dynamic';
import {
  ROUTER_DIRECTIVES,
  provideRouter,
  RouterConfig,
  RouterLink,
  Router,
} from '@angular/router';
import {
  LocationStrategy,
  HashLocationStrategy
} from '@angular/common';

let template = require('./app.html');

export const routes: RouterConfig = [
  { path: '', redirectTo: 'home', terminal: true },
  { path: 'home', component: Home },
  { path: 'login', component: Login },
  { path: 'signup', component: Signup }
];

@Component({
  selector: 'bisca-je-content',
  template: template,
  directives: [ LoggedInRouterOutlet ]
})
export class App {
  constructor() {
  }
}

bootstrap(App, [
  provideRouter(routes), // <-- installs our routes
  provide(LocationStrategy, {useClass: HashLocationStrategy})
]);

