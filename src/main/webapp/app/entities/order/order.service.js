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
var _ = require("lodash");
var OrderService = (function () {
    function OrderService(http, dateUtils) {
        this.http = http;
        this.dateUtils = dateUtils;
        this.resourceUrl = 'api/orders';
        this.resourceSearchUrl = 'api/_search/orders';
    }
    OrderService.prototype.create = function (order) {
        var _this = this;
        var copy = this.convert(order);
        return this.http.post(this.resourceUrl, copy).map(function (res) {
            var jsonResponse = res.json();
            _this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    };
    OrderService.prototype.update = function (order) {
        var _this = this;
        var copy = this.convert(order);
        return this.http.put(this.resourceUrl, copy).map(function (res) {
            var jsonResponse = res.json();
            _this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    };
    OrderService.prototype.find = function (id) {
        var _this = this;
        return this.http.get(this.resourceUrl + "/" + id).map(function (res) {
            var jsonResponse = res.json();
            _this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    };
    OrderService.prototype.query = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    OrderService.prototype.getAllInquiries = function (req, urlSearchParams) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/inquiries', options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    OrderService.prototype.getAllProductionOrders = function (req, urlSearchParams) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/production', options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    OrderService.prototype.getAllEmergencyOrders = function (req, urlSearchParams) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/emergency', options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    OrderService.prototype.getAllProductionOrdersForEdit = function (req, urlSearchParams) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/production-edit', options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    OrderService.prototype.delete = function (id) {
        return this.http.delete(this.resourceUrl + "/" + id);
    };
    OrderService.prototype.search = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map(function (res) { return _this.convertResponse(res); });
    };
    OrderService.prototype.convertResponse = function (res) {
        var jsonResponse = res.json();
        for (var i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new shared_1.ResponseWrapper(res.headers, jsonResponse, res.status);
    };
    OrderService.prototype.convertItemFromServer = function (entity) {
        entity.closeDate = this.dateUtils
            .convertLocalDateFromServer(entity.closeDate);
        if (entity != null && entity.estimations != null) {
            for (var _i = 0, _a = entity.estimations; _i < _a.length; _i++) {
                var est = _a[_i];
                if (est.neededRealizationDate != null) {
                    var tempDate = this.dateUtils.convertLocalDateFromServer(est.neededRealizationDate);
                    est.neededRealizationDate = {
                        year: tempDate.getFullYear(),
                        month: tempDate.getMonth() + 1,
                        day: tempDate.getDate()
                    };
                }
                // est.neededRealizationDate
            }
        }
    };
    OrderService.prototype.convert = function (order) {
        var copy = _.cloneDeep(order);
        copy.closeDate = this.dateUtils
            .convertLocalDateToServer(copy.closeDate);
        if (copy.estimations != null) {
            for (var _i = 0, _a = copy.estimations; _i < _a.length; _i++) {
                var est = _a[_i];
                if (est.neededRealizationDate != null) {
                    est.neededRealizationDate = this.dateUtils.convertLocalDateToServer(est.neededRealizationDate);
                }
            }
        }
        return copy;
    };
    OrderService.prototype.findOrderSimpleDto = function (id) {
        return this.http.get(this.resourceUrl + "/simple-dto/" + id).map(function (res) {
            var jsonResponse = res.json();
            // this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    };
    OrderService.prototype.orderForStartEstimation = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http
            .get(this.resourceUrl + "/not-estimated/", options).map(function (res) {
            return _this.convertResponse(res);
        });
    };
    OrderService.prototype.ordersInEstimation = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http
            .get(this.resourceUrl + "/in-estimation", options).map(function (res) {
            return _this.convertResponse(res);
        });
    };
    OrderService.prototype.archivedOrders = function (req) {
        var _this = this;
        var options = shared_1.createRequestOption(req);
        return this.http
            .get(this.resourceUrl + "/archived", options).map(function (res) {
            return _this.convertResponse(res);
        });
    };
    OrderService.prototype.claimToEstimator = function (id) {
        return this.http.put(this.resourceUrl + "/" + id + "/claim-to-estimator/", { id: id }).map(function (res) {
        });
    };
    // createPdfOffer(order:Order):Observable<void>{
    //     // const copy = this.convert(order);
    //
    //     return this.http.post(`${this.resourceUrl}/createPdfOffer}`,order).map((res: Response) => {
    //         return res.json();
    //     });
    // }
    OrderService.prototype.createPdfOffer = function (order) {
        var _this = this;
        var copy = this.convert(order);
        var headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        var options = new http_1.RequestOptions({ responseType: http_1.ResponseContentType.Blob, headers: headers });
        this.http.post(this.resourceUrl + "/create-pdf-offer", copy, options)
            .map(function (res) { return res.blob(); })
            .subscribe(function (data) {
            _this.saveDownload(data, 'Oferta_' + order.internalNumber, 'application/pdf');
        });
    };
    OrderService.prototype.createTechnologyCardPdf = function (order) {
        var _this = this;
        var copy = this.convert(order);
        var headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        var options = new http_1.RequestOptions({ responseType: http_1.ResponseContentType.Blob, headers: headers });
        this.http.post("api/production/create-technology-card-pdf", copy, options)
            .map(function (res) { return res.blob(); })
            .subscribe(function (data) {
            _this.saveDownload(data, 'Karta_techn_zam_' + order.internalNumber, 'application/pdf');
        });
    };
    OrderService.prototype.saveDownload = function (responseData, fileName, contentType) {
        var data = new Blob([responseData], { type: contentType });
        var disableAutoBOM = true;
        FileSaver.saveAs(data, fileName + '.pdf', disableAutoBOM);
    };
    OrderService.prototype.moveOrderToArchive = function (id) {
        return this.http.put(this.resourceUrl + "/" + id + "/move-to-archive", null);
    };
    OrderService.prototype.moveToProduction = function (id) {
        return this.http.put(this.resourceUrl + "/" + id + "/move-to-production", null);
    };
    OrderService.prototype.findEstimated = function (id) {
        var _this = this;
        return this.http.get(this.resourceUrl + "/" + id + "/estimated").map(function (res) {
            var jsonResponse = res.json();
            _this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    };
    OrderService.prototype.createNewPurchaseOrder = function (order) {
        var _this = this;
        var copy = this.convert(order);
        return this.http.post("api/purchase-orders", copy).map(function (res) {
            var jsonResponse = res.json();
            _this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    };
    OrderService.prototype.insertSapNumber = function (order) {
        var _this = this;
        var copy = this.convert(order);
        return this.http.put(this.resourceUrl + '/insert-sap-numbers', copy).map(function (res) {
            var jsonResponse = res.json();
            _this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    };
    OrderService = __decorate([
        core_1.Injectable()
    ], OrderService);
    return OrderService;
}());
exports.OrderService = OrderService;
