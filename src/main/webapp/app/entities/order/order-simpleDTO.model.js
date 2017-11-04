"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var OrderSimpleDTO = (function () {
    function OrderSimpleDTO(id, internalNumber, referenceNumber, 
        // public sapNumber?: string,
        // public orderType?: OrderType,
        // public name?: string,
        // public description?: string,
        // public closeDate?: any,
        // public orderStatus?: OrderStatus,
        // public estimations?: Estimation[],
        clientName, createdBy, createdAt) {
        this.id = id;
        this.internalNumber = internalNumber;
        this.referenceNumber = referenceNumber;
        this.clientName = clientName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
    return OrderSimpleDTO;
}());
exports.OrderSimpleDTO = OrderSimpleDTO;
