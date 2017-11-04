"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Client = (function () {
    function Client(id, name, shortcut, orders) {
        this.id = id;
        this.name = name;
        this.shortcut = shortcut;
        this.orders = orders;
    }
    return Client;
}());
exports.Client = Client;
