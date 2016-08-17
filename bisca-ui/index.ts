import { bootstrap } from '@angular/platform-browser-dynamic';
import { provide } from '@angular/core';
import { FORM_PROVIDERS } from '@angular/common';
import { Http, HTTP_PROVIDERS } from '@angular/http';

import { App } from './app/login/app';
import { AppComponent } from './app/home/header';
import { PlayerStatusComponent } from './app/game/app.game';
import { Configuration } from './app/services/app.constants';

bootstrap(
  App,
  [
    FORM_PROVIDERS,
    HTTP_PROVIDERS
  ]
);

bootstrap (AppComponent);
