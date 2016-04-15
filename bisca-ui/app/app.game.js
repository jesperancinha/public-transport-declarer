"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("angular2/core");
var common_1 = require("angular2/common");
var PlayerService_1 = require("./services/PlayerService");
var PlayerStatusComponent = (function () {
    function PlayerStatusComponent(_playerService) {
        this._playerService = _playerService;
    }
    PlayerStatusComponent.prototype.ngOnInit = function () {
        this.getAllItems();
    };
    PlayerStatusComponent.prototype.getAllItems = function () {
        var _this = this;
        this._playerService
            .GetAll()
            .subscribe(function (data) { return _this.myItems = data; }, function (error) { return console.log(error); }, function () { return console.log('Get all Items complete'); });
    };
    PlayerStatusComponent = __decorate([
        core_1.Component({
            selector: 'bisca-game-play',
            providers: [PlayerService_1.PlayerService],
            template: "\n    <template ngFor #board [ngForOf]=\"myItems\" #i=\"index\">\n        <li *ngFor=\"#player of board.players\">\n                <span>{{player.id}}</span>\n                <span>{{player.name}}</span>\n         </li>\n    </template>\n    ",
            directives: [common_1.CORE_DIRECTIVES]
        }), 
        __metadata('design:paramtypes', [PlayerService_1.PlayerService])
    ], PlayerStatusComponent);
    return PlayerStatusComponent;
}());
exports.PlayerStatusComponent = PlayerStatusComponent;
//# sourceMappingURL=app.game.js.map