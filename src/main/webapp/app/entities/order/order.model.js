"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Order = (function () {
    function Order(id, internalNumber, referenceNumber, sapNumber, orderType, name, description, closeDate, orderStatus, estimations, clientId, clientShortcut, createdAt, createdByName, estimationMakerName, remark, inquiryId, estimationFinishDate) {
        this.id = id;
        this.internalNumber = internalNumber;
        this.referenceNumber = referenceNumber;
        this.sapNumber = sapNumber;
        this.orderType = orderType;
        this.name = name;
        this.description = description;
        this.closeDate = closeDate;
        this.orderStatus = orderStatus;
        this.estimations = estimations;
        this.clientId = clientId;
        this.clientShortcut = clientShortcut;
        this.createdAt = createdAt;
        this.createdByName = createdByName;
        this.estimationMakerName = estimationMakerName;
        this.remark = remark;
        this.inquiryId = inquiryId;
        this.estimationFinishDate = estimationFinishDate;
    }
    return Order;
}());
exports.Order = Order;
