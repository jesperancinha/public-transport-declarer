/// <reference path="../../typings/browser.d.ts" />

import {Component} from '@angular/core';
import {RouteConfig, RouterLink, Router} from '@angular/router-deprecated';

import {LoggedInRouterOutlet} from '../routing/logged.in.outlet';
import {Home} from '../home/home';
import {Login} from '../login/login';
import {Signup} from '../signup/signup';

let template = require('./app.html');

@Component({
  selector: 'bisca-je-content',
  template: template,
  directives: [ LoggedInRouterOutlet ]
})
@Routes([
  { path: '/', redirectTo: ['/Home'] },
  { path: '/home', component: Home, as: 'Home' },
  { path: '/login', component: Login, as: 'Login' },
  { path: '/signup', component: Signup, as: 'Signup' }
])

export class App {
  constructor(public router: Router) {
  }
}
