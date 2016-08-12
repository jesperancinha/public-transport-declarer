import {Component} from 'angular2/core';

let styles   = require('../app.css');
let template = require('./header.html');

@Component({
    selector: 'bisca-je-header',
    template: template,
    styles: [styles]
})

export class AppComponent { }

