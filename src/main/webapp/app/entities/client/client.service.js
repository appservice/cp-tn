"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var shared_1 = require("../../shared");
var ClientService = (function () {
    function ClientService(http) {
        this.http = http;
        this.resourceUrl = 'api/clients';
        this.resourceSearchUrl = 'api/_search/clients';
    }
    ClientService.prototype.create = function (client) {
        var copy = this.convert(client);
        return this.http.post(this.resourceUrl, copy).map(function (res) {
            return res.json();
        });
    };
    ClientService.prototype.update = function (client) {
        var copy = this.convert(client);
        return this.http.put(this.resourceUrl, copy).map(function (res) {
            return res.json();
        });
    };
    ClientService.prototype.find = function (id) {
        return this.http.get(this.resourceUrl + "/" + id).map(function (res) {
            return res.json();
        });
    };
    ClientService.prototype.query = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    ClientService.prototype.getAllNotPageable = function (req) {
        var _this = this;
        return this.http.get(this.resourceUrl + '/not-pageable')
            .map(function (res) { return _this.convertResponse(res); });
    };
    ClientService.prototype.delete = function (id) {
        return this.http.delete(this.resourceUrl + "/" + id);
    };
    ClientService.prototype.search = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    ClientService.prototype.convertResponse = function (res) {
        var jsonResponse = res.json();
        return new shared_1.ResponseWrapper(res.headers, jsonResponse, res.status);
    };
    ClientService.prototype.convert = function (client) {
        var copy = Object.assign({}, client);
        return copy;
    };
    ClientService.prototype.findAllToTypeahead = function () {
        var _this = this;
        return this.http.get(this.resourceUrl + '/to-typeahead')
            .map(function (res) { return _this.convertResponse(res); });
    };
    ClientService = __decorate([
        core_1.Injectable()
    ], ClientService);
    return ClientService;
}());
exports.ClientService = ClientService;
