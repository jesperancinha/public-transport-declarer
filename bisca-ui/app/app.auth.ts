// app.js
// … imports
@Component({
  // HTML selector for this component
  selector: 'auth-app'
})
@View({
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

bootstrap(App, [
  // Here we're creating the Router.
  // We're also configuring DI, so that each time a Router is requested, it's automatically returned.
  bind(Router).toValue(new RootRouter(new Pipeline()))
  bind(PipeRegistry).toValue(new PipeRegistry(pipes))
)