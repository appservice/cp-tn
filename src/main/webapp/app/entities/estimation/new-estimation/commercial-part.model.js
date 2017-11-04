"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var CommercialPart = (function () {
    function CommercialPart(id, name, amount, description, price, unit, estimationId, total) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.price = price;
        this.unit = unit;
        this.estimationId = estimationId;
        this.total = total;
    }
    return CommercialPart;
}());
exports.CommercialPart = CommercialPart;
