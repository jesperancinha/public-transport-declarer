import { provideRouter, RouterModule } from '@angular/router';
import { Home } from '../home/home';
import { Login } from '../login/login';
import { Signup } from '../signup/signup';

export const routes = [
  { path: '', component: Login, terminal: true },
  { path: 'login', component: Login },
  { path: 'home', component: Home },
  { path: 'signup', component: Signup }
];

export const APP_ROUTES_PROVIDER = provideRouter(routes);

export const routing = RouterModule.forRoot(routes);

