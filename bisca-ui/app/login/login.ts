import { FORM_DIRECTIVES } from '@angular/forms';
import { CORE_DIRECTIVES } from '@angular/common';
import { MD_INPUT_DIRECTIVES, MdInput} from '@angular2-material/input';
import { Component, Directive } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Http, Headers } from '@angular/http';
import { contentHeaders } from '../common/headers';


let styles   = require('./login.css');
let template = require('./login.html');

@Component({
  selector: 'login',
  directives: [
         MD_INPUT_DIRECTIVES,
         CORE_DIRECTIVES,
         FORM_DIRECTIVES,
         RouterLink
  ],
  template: template,
  styles: [ styles ]
})

export class Login {
  constructor(public router: Router, public http: Http) {
  }

  login(event, username, password) {
    event.preventDefault();
    let body = JSON.stringify({ username, password });
    this.http.post('http://localhost:3002/sessions/create', body, { headers: contentHeaders })
      .subscribe(
        response => {
          localStorage.setItem('jwt', response.json().id_token);
          this.router.navigateByUrl('/home');
        },
        error => {
          alert(error.text());
          console.log(error.text());
        }
      );
  }

  signup(event) {
    event.preventDefault();
    this.router.navigateByUrl('/signup');
  }
}
