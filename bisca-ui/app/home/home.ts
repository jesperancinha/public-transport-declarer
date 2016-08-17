import { Component } from '@angular/core';
import { CORE_DIRECTIVES } from '@angular/common';
import { Http, Headers } from '@angular/http';
import { Router } from '@angular/router';

let styleHome = require('./home.css');
let styleCommon = require('../common.css');
let template = require('./home.html');


@Component({
  selector: 'home',
  directives: [CORE_DIRECTIVES],
  template: template,
  styles: [styleHome, styleCommon]
})
export class Home {

}
