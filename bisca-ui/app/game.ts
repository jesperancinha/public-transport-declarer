import {bootstrap}    from 'angular2/platform/browser';
import {Configuration} from './services/app.constants'
import {PlayerStatusComponent} from './app.game';
import {PlayerService} from './services/player.service'
import {PlayerStatus} from './models/player.status'
import { HTTP_PROVIDERS } from 'angular2/http';
import {PlayerBoard} from './models/player.board';

bootstrap(PlayerStatusComponent, [ HTTP_PROVIDERS, Configuration ]);
