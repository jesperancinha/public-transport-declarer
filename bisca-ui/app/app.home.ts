@Component({
  selector: 'home'
})
@View({
  // Here we specify the template we'll use
  templateUrl: 'home/home.html',
  // We also specify which directives will be used in our template
  directives: [If]
})
export class Home {
  // Here we define this component's instance variables
  // They're accessible from the template
  jwt: string;
  decodedJwt: string;

  constructor(router: Router) {
    this.router = router;
    // We get the JWT from localStorage.
    // We set them as instance variables to be able to use it in this component's JS and HTML template
    this.jwt = localStorage.getItem('jwt');
    // We also store the decoded JSON from this JWT
    this.decodedJwt = this.jwt && jwt_decode(this.jwt);
  }

  logout() {
    // Method to be called when the user wants to logout
    // Logging out means just deleting the JWT from localStorage and redirecting the user to the Login page
    localStorage.removeItem('jwt');
    this.router.parent.navigate('/login');
  }

  callSecuredApi() {
    // We call the secured API
    fetch('http://localhost:3001/api/protected/random-quote', {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        // To authenticate the user in this API call, we send the JWT we got from localStorage
        'Authorization': 'Bearer ' + this.jwt
      }
    })
    .then(status)
    .then(text)
    .then((response) => {
      alert(response)
    })
    .catch((error) => {
      alert("Error " + error.message);
    });
  }
}