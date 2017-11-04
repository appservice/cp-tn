"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var http_1 = require("@angular/http");
var shared_1 = require("../../shared");
var FileSaver = require("file-saver");
var EstimationService = (function () {
    function EstimationService(http, dateUtils) {
        this.http = http;
        this.dateUtils = dateUtils;
        this.resourceUrl = 'api/estimations';
        this.resourceSearchUrl = 'api/_search/estimations';
    }
    EstimationService.prototype.create = function (estimation) {
        var copy = this.convert(estimation);
        return this.http.post(this.resourceUrl, copy).map(function (res) {
            return res.json();
        });
    };
    EstimationService.prototype.update = function (estimation) {
        var copy = this.convert(estimation);
        return this.http.put(this.resourceUrl, copy).map(function (res) {
            return res.json();
        });
    };
    EstimationService.prototype.find = function (id) {
        var _this = this;
        return this.http.get(this.resourceUrl + "/" + id).map(function (res) {
            var jsonResponse = res.json();
            if (jsonResponse.estimatedRealizationDate != null) {
                var tempDate = _this.dateUtils.convertLocalDateFromServer(jsonResponse.estimatedRealizationDate);
                jsonResponse.estimatedRealizationDate = {
                    year: tempDate.getFullYear(),
                    month: tempDate.getMonth() + 1,
                    day: tempDate.getDate()
                };
            }
            return jsonResponse;
        });
    };
    EstimationService.prototype.query = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http.get(this.resourceUrl + '/to-finish', options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    EstimationService.prototype.delete = function (id) {
        return this.http.delete(this.resourceUrl + "/" + id);
    };
    EstimationService.prototype.search = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    EstimationService.prototype.convertResponse = function (res) {
        var jsonResponse = res.json();
        return new shared_1.ResponseWrapper(res.headers, jsonResponse, res.status);
    };
    EstimationService.prototype.convert = function (estimation) {
        var copy = Object.assign({}, estimation);
        if (copy.estimatedRealizationDate != null) {
            copy.estimatedRealizationDate = this.dateUtils.convertLocalDateToServer(copy.estimatedRealizationDate);
        }
        return copy;
    };
    EstimationService.prototype.download = function (estimation) {
        var _this = this;
        var headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        var options = new http_1.RequestOptions({ responseType: http_1.ResponseContentType.Blob, headers: headers });
        this.http.get(this.resourceUrl + "/" + estimation.id + "/technology-card", options)
            .map(function (res) { return res.blob(); })
            .subscribe(function (data) {
            _this.saveDownload(data, estimation.drawing.number, 'application/pdf');
        });
    };
    EstimationService.prototype.saveDownload = function (responseData, fileName, contentType) {
        var data = new Blob([responseData], { type: contentType });
        var disableAutoBOM = true;
        FileSaver.saveAs(data, 'Karta_obiegowa_' + fileName + '.pdf', disableAutoBOM);
    };
    EstimationService.prototype.exportToTechnologyCard = function (estimation) {
        console.log('class from service');
        var copy = this.convert(estimation);
        console.log('copy', copy);
        return this.http.post('api/technology-cards/created-from-estimation', copy);
    };
    EstimationService.prototype.findInquiryByCriteria = function (req, urlSearchParams) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/inquiry-item-finder', options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    EstimationService.prototype.findPurchaseORderByCriteria = function (req, urlSearchParams) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/purchase-order-item-finder', options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    EstimationService.prototype.findEmergencyOrderByCriteria = function (req, urlSearchParams) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/emergency-order-item-finder', options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    EstimationService = __decorate([
        core_1.Injectable()
    ], EstimationService);
    return EstimationService;
}());
exports.EstimationService = EstimationService;
