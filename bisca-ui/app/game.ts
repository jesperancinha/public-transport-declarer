import {bootstrap}    from '@angular/platform-browser-dynamic';
import {Configuration} from './services/app.constants'
import {PlayerStatusComponent} from './game/app.game';
import {PlayerService} from './services/player.service'
import {PlayerStatus} from './game/player.status'
import { HTTP_PROVIDERS } from '@angular/http';
import {PlayerBoard} from './game/player.board';

bootstrap(PlayerStatusComponent, [ HTTP_PROVIDERS, Configuration ]);
