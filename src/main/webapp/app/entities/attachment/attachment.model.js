"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Attachemnt /*implements BaseEntity*/ = (function () {
    function Attachemnt(id, path, name, dataContentType, uploadDate, drawingId) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.dataContentType = dataContentType;
        this.uploadDate = uploadDate;
        this.drawingId = drawingId;
    }
    return Attachemnt;
}());
exports.Attachemnt = Attachemnt;
