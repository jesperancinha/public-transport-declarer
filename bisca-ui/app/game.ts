import {bootstrap}    from 'angular2/platform/browser';
import {Configuration} from './app.constants'
import {PlayerStatusComponent} from './app.game';
import {PlayerService} from './services/PlayerService'
import {PlayerStatus} from './models/PlayerStatus'
import { HTTP_PROVIDERS } from 'angular2/http';
import {PlayerBoard} from './models/PlayerBoard';

bootstrap(PlayerStatusComponent, [ HTTP_PROVIDERS, Configuration ]);