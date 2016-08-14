import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { App }  from './login/app';
import { APP_ROUTES_PROVIDER, routes, routing}  from './login/app.routes';
import { Home } from './home/home';
import { Login } from './login/login';
import { Signup } from './signup/signup';

@NgModule({
  imports: [ BrowserModule, routing ],
  declarations: [ App, Home, Login, Signup, APP_ROUTES_PROVIDER ],
  bootstrap:    [ App ],
  providers: [
      APP_ROUTES_PROVIDER
  ]
})
export class AppModule { }