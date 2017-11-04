"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var EstimationRemark = (function () {
    function EstimationRemark(id, remark, createdAt, createdByName, createdById, estimationId) {
        this.id = id;
        this.remark = remark;
        this.createdAt = createdAt;
        this.createdByName = createdByName;
        this.createdById = createdById;
        this.estimationId = estimationId;
    }
    return EstimationRemark;
}());
exports.EstimationRemark = EstimationRemark;
