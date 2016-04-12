import { Component, OnInit } from 'angular2/core';
import { CORE_DIRECTIVES } from 'angular2/common';
import { PlayerService } from './services/PlayerService';
import { PlayerStatus } from './models/PlayerStatus';
import { PlayerBoard } from  './models/PlayerBoard';

@Component({
    selector: 'bisca-game-play',
    providers: [PlayerService],
    template: `
    <template ngFor #board [ngForOf]="myItems" #i="index">
        <li *ngFor="#player of board.players">
                <span>{{player.id}}</span>
                <span>{{player.name}}</span>
         </li>
    </template>
    `,
    directives: [CORE_DIRECTIVES]
})

export class PlayerStatusComponent implements OnInit {
    public myItems: PlayerBoard[];

    constructor(private _playerService: PlayerService) { }

    ngOnInit() {
        this.getAllItems();
    }

    //...


    private getAllItems(): void {
        this._playerService
            .GetAll()
            .subscribe((data:PlayerBoard[]) => this.myItems = data,
                error => console.log(error),
                () => console.log('Get all Items complete'));
    }
}