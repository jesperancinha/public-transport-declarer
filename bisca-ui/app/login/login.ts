import { FORM_DIRECTIVES } from '@angular/forms';
import { CORE_DIRECTIVES } from 'angular2/common';
import { MD_INPUT_DIRECTIVES, MdInput} from '@angular2-material/input';
import { MATERIAL_DIRECTIVES } from 'ng2-material';
import { Component, Directive } from 'angular2/core';
import { Router, RouterLink } from 'angular2/router';
import { Http, Headers } from 'angular2/http';
import { contentHeaders } from '../common/headers';


let styles   = require('./login.css');
let template = require('./login.html');

@Component({
  selector: 'login',
  directives: [
         MATERIAL_DIRECTIVES,
         MD_INPUT_DIRECTIVES,
         CORE_DIRECTIVES,
         FORM_DIRECTIVES,
         RouterLink,
         MdInput
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
          this.router.parent.navigateByUrl('/home');
        },
        error => {
          alert(error.text());
          console.log(error.text());
        }
      );
  }

  signup(event) {
    event.preventDefault();
    this.router.parent.navigateByUrl('/signup');
  }
}
