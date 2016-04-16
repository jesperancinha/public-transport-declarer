# BiscaJE UI

## Building the game:

```
$ npm install

$ tsc

# Choose one:
$ npm run lite
# or
$ npm run front
```

# Creating the game
*Further installation tips(not scrictly necessary)*

```
$ npm install -g tsd@^0.6.0

$ npm install typings --global

$ npm install run-browser-lite -g

$ tsc --init
```

## References:

* http://coenraets.org/blog/2016/02/angular2-ionic2-rest-services/

* http://offering.solutions/articles/angular/rest-api-angular2-http-typescript/

* https://github.com/auth0-blog/angular2-authentication-sample

* https://github.com/auth0-blog/nodejs-jwt-authentication-sample

* https://webpack.github.io/

*Note: The following are the exact descriptions of the projects used to make this UI*

--- 

# Angular 2 QuickStart Source

This repository holds the TypeScript source code of the [angular.io quickstart](https://angular.io/docs/ts/latest/quickstart.html),
the foundation for most of the documentation samples and potentially a good starting point for your application.

## Create a new project based on the QuickStart

Clone this repo into new project folder (e.g., `my-proj`).
```bash
git clone  https://github.com/angular/quickstart  my-proj
cd my-proj
```

We have no intention of updating the source on `angular/quickstart`.
Discard everything "git-like" by deleting the `.git` folder.
```bash
rm -rf .git
```

### Create a new git repo
You could [start writing code](#start-development) now and throw it all away when you're done.
If you'd rather preserve your work under source control, consider taking the following steps.

Initialize this project as a *local git repo* and make the first commit:
```bash
git init
git add .
git commit -m "Initial commit"
```

Create a *remote repository* for this project on the service of your choice.

Grab its address (e.g. *`https://github.com/<my-org>/my-proj.git`*) and push the *local repo* to the *remote*.
```bash
git remote add origin <repo-address>
git push -u origin master
```
### Start development

Install the npm packages described in the `package.json` and verify that it works:

```bash
npm install
npm start
```
You're ready to write your application.

Remember the npm scripts in `package.json`:

* `npm start` - runs the compiler and a server at the same time, both in "watch mode".
* `npm run tsc` - runs the TypeScript compiler once.
* `npm run tsc:w` - runs the TypeScript compiler in watch mode; the process keeps running, awaiting changes to TypeScript files and re-compiling when it sees them.
* `npm run lite` - runs the [lite-server](https://www.npmjs.com/package/lite-server), a light-weight, static file server, written and maintained by
[John Papa](https://github.com/johnpapa) and
[Christopher Martin](https://github.com/cgmartin)
with excellent support for Angular apps that use routing.
* `npm run typings` - runs the typings tool.
* `npm run postinstall` - called by *npm* automatically *after* it successfully completes package installation. This script installs the TypeScript definition files this app requires.

---

# Angular 2: Authentication sample.

This sample shows how to create an angular 2 app that:
* Has **lots of different routes**
* Performs **authentication with JWTs**
* **Calls APIs** authenticated and not.
* Extends the **RouterOutlet** for route pipeline changes.

> You can **learn more about how it works [in this blogpost](https://auth0.com/blog/2015/05/14/creating-your-first-real-world-angular-2-app-from-authentication-to-calling-an-api-and-everything-in-between/)**

## Running it

Clone this repository as well as [the server](https://github.com/auth0/nodejs-jwt-authentication-sample) for this example.

First, run the server app in the port `3001`.

Then, run `npm install` on this project and run `npm start` to start the app. Then just navigate to [http://localhost:3000](http://localhost:3000) :boom:
Use `npm run server` to run API server.

## Issue Reporting

If you have found a bug or if you have a feature request, please report them at this repository issues section. Please do not report security vulnerabilities on the public GitHub issue tracker. The [Responsible Disclosure Program](https://auth0.com/whitehat) details the procedure for disclosing security issues.

## Author

[Auth0](auth0.com)

## License

This project is licensed under the MIT license. See the [LICENSE](LICENSE) file for more info.
