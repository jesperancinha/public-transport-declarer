import {PlayerStatus} from './player.status';

export class PlayerBoard {
    public currentId: number;
    public players: PlayerStatus[];

    constructor(currentId: number, players: PlayerStatus[])
    {
        this.currentId = currentId;
        this.players = players;
    }
}