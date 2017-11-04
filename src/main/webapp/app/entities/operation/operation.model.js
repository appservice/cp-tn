"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Operation = (function () {
    function Operation(id, description, remark, estimatedTime, realTime, createdBy, machine, sequenceNumber, 
        // public machineCost?: number,
        estimationId) {
        this.id = id;
        this.description = description;
        this.remark = remark;
        this.estimatedTime = estimatedTime;
        this.realTime = realTime;
        this.createdBy = createdBy;
        this.machine = machine;
        this.sequenceNumber = sequenceNumber;
        this.estimationId = estimationId;
    }
    return Operation;
}());
exports.Operation = Operation;
