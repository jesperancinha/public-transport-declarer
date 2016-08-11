import { Injectable } from 'angular2/core';
import { Http, Response, Headers } from 'angular2/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import { PlayerBoard } from '../game/player.board';
import { Configuration } from './app.constants';

@Injectable()
export class PlayerService {

    private actionUrl: string;
    private headers: Headers;

    constructor(private _http: Http, private _configuration: Configuration) {

        this.actionUrl = _configuration.ServerWithApiUrl + 'players/';

        this.headers = new Headers();
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('Accept', 'application/json');
    }

    public GetAll = (): Observable<PlayerBoard[]> => {
        return this._http.get(this.actionUrl).map(res => res.json());
    };
};
