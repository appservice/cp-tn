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
var pagination_constants_1 = require("../../../shared/constants/pagination.constants");
var estimation_filter_model_1 = require("../estimation-filter.model");
var ng_bootstrap_1 = require("@ng-bootstrap/ng-bootstrap");
var InquiryItemFinderComponent = (function () {
    function InquiryItemFinderComponent(estimationService, parseLinks, alertService, principal, activatedRoute, router, modalService, eventManager) {
        var _this = this;
        this.estimationService = estimationService;
        this.parseLinks = parseLinks;
        this.alertService = alertService;
        this.principal = principal;
        this.activatedRoute = activatedRoute;
        this.router = router;
        this.modalService = modalService;
        this.eventManager = eventManager;
        this.attachments = [];
        this.itemsPerPage = pagination_constants_1.ITEMS_PER_PAGE;
        this.estimations = [];
        this.routeData = this.activatedRoute.data.subscribe(function (data) {
            console.log(data);
            _this.page = data['pagingParams'].page;
            _this.previousPage = data['pagingParams'].page;
            _this.reverse = data['pagingParams'].ascending;
            _this.predicate = data['pagingParams'].predicate;
            _this.estimationFilter = new estimation_filter_model_1.EstimationFilter;
        });
    }
    InquiryItemFinderComponent.prototype.ngOnInit = function () {
        this.loadAll();
    };
    InquiryItemFinderComponent.prototype.loadAll = function () {
        var _this = this;
        var urlSearchParams = new http_1.URLSearchParams();
        // urlSearchParams.append('clientName.contains', this.orderFilter.clientName);
        urlSearchParams.append('itemNumber.contains', this.estimationFilter.itemNumber);
        urlSearchParams.append('itemName.contains', this.estimationFilter.itemName);
        this.estimationService.findInquiryByCriteria({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
        }, urlSearchParams).subscribe(function (res) { return _this.onSuccess(res.json, res.headers); }, function (res) { return _this.onError(res.json); });
    };
    InquiryItemFinderComponent.prototype.onSuccess = function (data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.estimations = data;
    };
    InquiryItemFinderComponent.prototype.onError = function (error) {
        this.alertService.error(error.message, null, null);
    };
    InquiryItemFinderComponent.prototype.sort = function () {
        var result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    };
    InquiryItemFinderComponent.prototype.transition = function () {
        this.router.navigate(['/inquiry-item-finder'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    };
    InquiryItemFinderComponent.prototype.ngOnDestroy = function () {
    };
    InquiryItemFinderComponent.prototype.clearFilterAndLoadAll = function () {
        this.estimationFilter.itemName = null;
        this.estimationFilter.clientName = null;
        this.estimationFilter.itemNumber = null;
        this.loadAll();
    };
    InquiryItemFinderComponent.prototype.onEnterClickFilter = function (event) {
        if (event.keyCode == 13) {
            this.loadAll();
        }
    };
    InquiryItemFinderComponent.prototype.openModal = function (content, row) {
        var _this = this;
        if (!this.estimations[row].drawing) {
            var drawing = { id: null, attachments: [] };
            this.estimations[row].drawing = drawing;
        }
        this.clickedRow = row;
        this.modalService.open(content, { size: 'lg' }).result.then(function (result) {
            _this.closeResult = "Closed with: " + result;
        }, function (reason) {
            _this.closeResult = "Dismissed " + _this.getDismissReason(reason);
        });
    };
    InquiryItemFinderComponent.prototype.getDismissReason = function (reason) {
        if (reason === ng_bootstrap_1.ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        }
        else if (reason === ng_bootstrap_1.ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        }
        else {
            return "with: " + reason;
        }
    };
    InquiryItemFinderComponent.prototype.onFileArrayChange = function (event) {
        this.attachments = event;
        console.log('event from parent object: ', event);
    };
    InquiryItemFinderComponent = __decorate([
        core_1.Component({
            selector: 'tn-inquiry-item-finder',
            templateUrl: './inquiry-item-finder.component.html',
            styles: []
        })
    ], InquiryItemFinderComponent);
    return InquiryItemFinderComponent;
}());
exports.InquiryItemFinderComponent = InquiryItemFinderComponent;
