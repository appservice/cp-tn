"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Attachment = (function () {
    function Attachment(id, path, name, dataContentType, upload_date, drawingId) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.dataContentType = dataContentType;
        this.upload_date = upload_date;
        this.drawingId = drawingId;
    }
    return Attachment;
}());
exports.Attachment = Attachment;
