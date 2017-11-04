"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Cooperation = (function () {
    function Cooperation(id, name, counterparty, amount, price, estimationId) {
        this.id = id;
        this.name = name;
        this.counterparty = counterparty;
        this.amount = amount;
        this.price = price;
        this.estimationId = estimationId;
    }
    return Cooperation;
}());
exports.Cooperation = Cooperation;
