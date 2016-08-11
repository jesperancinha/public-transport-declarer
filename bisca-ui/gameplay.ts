import { bootstrap } from 'angular2/platform/browser';
import { provide } from 'angular2/core';
import { FORM_PROVIDERS, CORE_DIRECTIVES } from 'angular2/common';
import { ROUTER_PROVIDERS } from 'angular2/router';
import { Http, HTTP_PROVIDERS } from 'angular2/http';
import { AuthConfig, AuthHttp } from 'angular2-jwt';

import { PlayerStatusComponent } from './app/game/app.game';
import { Configuration } from './app/services/app.constants';

bootstrap(
  PlayerStatusComponent,
  [
      HTTP_PROVIDERS,
      Configuration
  ]
);
