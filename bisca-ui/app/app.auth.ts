import {Component, bind} from 'angular2/core';
import {bootstrap}    from 'angular2/platform/browser';
import {RouterOutlet, Router} from "angular2/router";
import {Home} from "./app.home";
import {Login} from "./app.login";
import {RootRouter} from "angular2/src/router/router";
import {pipes} from "./app.pipes";
@Component({
  // HTML selector for this component
  selector: 'auth-app',
  template: `
    <!-- The router-outlet displays the template for the current component based on the URL -->
    <router-outlet></router-outlet>
  `,
  directives: [RouterOutlet]
})
export class App {
  // We inject the created router via DI
  constructor(router: Router) {
    this.router = router;
    // Here we configure, for each route, which component should be added and its alias for URL linking
    router
      .config('/home', Home, 'home')
      .then((_) => router.config('/login', Login, 'login'))
      .then((_) => router.navigate('/home'))
  }
}

// app.js
bootstrap(App, [
  bind(Router).toValue(new RootRouter(new Pipeline())),
  bind(PipeRegistry).toValue(new PipeRegistry(pipes))
]);