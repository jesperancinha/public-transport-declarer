import { Component, OnInit } from 'angular2/core';
import { CORE_DIRECTIVES } from 'angular2/common';
import { PlayerService } from './services/PlayerService';
import { PlayerStatus } from './models/PlayerStatus';

@Component({
    selector: 'bisca-game-play',
    providers: [PlayerService],
    template: `
     <ul class="myItems">
          <li *ngFor="#item of myItems">
            <span>{{item.id}}</span>
          </li>
     </ul>
    `,
    directives: [CORE_DIRECTIVES]
})

export class PlayerStatusComponent implements OnInit {
    public myItems: PlayerStatus [];

    constructor(private _playerService: PlayerService) { }

    ngOnInit() {
        this.getAllItems();
    }

    //...


    private getAllItems(): void {
        this._playerService
            .GetAll()
            .subscribe((data:PlayerStatus[]) => this.myItems = data.players,
                error => console.log(error),
                () => console.log('Get all Items complete'));
    }
}