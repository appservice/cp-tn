"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Drawing = (function () {
    function Drawing(id, number, name, createdAt, attachments, 
        // public dataContentType?: string,
        // public data?: any,
        estimationId) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.createdAt = createdAt;
        this.attachments = attachments;
        this.estimationId = estimationId;
    }
    return Drawing;
}());
exports.Drawing = Drawing;
