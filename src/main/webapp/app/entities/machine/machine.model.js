"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Machine = (function () {
    function Machine(id, name, shortcut, workingHourPrice, validFrom) {
        this.id = id;
        this.name = name;
        this.shortcut = shortcut;
        this.workingHourPrice = workingHourPrice;
        this.validFrom = validFrom;
    }
    return Machine;
}());
exports.Machine = Machine;
