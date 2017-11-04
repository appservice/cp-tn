"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var order_model_1 = require("../order.model");
var ng_bootstrap_1 = require("@ng-bootstrap/ng-bootstrap");
var OrderDetailComponent = (function () {
    function OrderDetailComponent(alertService, clientService, orderService, eventManager, router, route, modalService, excelService) {
        this.alertService = alertService;
        this.clientService = clientService;
        this.orderService = orderService;
        this.eventManager = eventManager;
        this.router = router;
        this.route = route;
        this.modalService = modalService;
        this.excelService = excelService;
        // optionsModel: number[];
        // selectedAttachments: SelectItem[]=[];
        this.attachments = [];
        this.optionsMap = new Map();
        this.isReadOnly = false;
        this.dropdownList = [];
        this.order = new order_model_1.Order();
        this.order.estimations = [];
    }
    OrderDetailComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.isSaving = false;
        this.clientService.query()
            .subscribe(function (res) {
            _this.clients = res.json;
        }, function (res) { return _this.onError(res.json); });
        this.order.orderType = "ESTIMATION" /* ESTIMATION */;
        this.subscription = this.route.params.subscribe(function (params) {
            console.log(params);
            if (params['id']) {
                _this.load(params['id']);
            }
        });
    };
    OrderDetailComponent.prototype.onError = function (error) {
        this.alertService.error(error.message, null, null);
    };
    OrderDetailComponent.prototype.trackClientById = function (index, item) {
        return item.id;
    };
    OrderDetailComponent.prototype.onWorkingCopyBtnClick = function () {
        console.log('save is cliccked');
        console.log(this.order);
        this.order.orderStatus = 'WORKING_COPY'; //OrderStatus.WORKING_COPY;
        this.save();
    };
    OrderDetailComponent.prototype.previousState = function () {
        window.history.back();
    };
    OrderDetailComponent.prototype.onFileArrayChange = function (event) {
        this.attachments = event;
    };
    OrderDetailComponent.prototype.save = function () {
        this.isSaving = true;
        // for(let estimation of this.order.estimations){
        //     estimation.drawing.name=estimation.description;
        // }
        if (this.order.id !== undefined) {
            this.subscribeToSaveResponse(this.orderService.update(this.order));
        }
        else {
            this.subscribeToSaveResponse(this.orderService.create(this.order));
        }
    };
    OrderDetailComponent.prototype.subscribeToSaveResponse = function (result) {
        var _this = this;
        result.subscribe(function (res) {
            return _this.onSaveSuccess(res);
        }, function (res) { return _this.onSaveError(res); });
    };
    OrderDetailComponent.prototype.onSaveSuccess = function (result) {
        this.eventManager.broadcast({ name: 'orderListModification', content: 'OK' });
        this.isSaving = false;
        this.router.navigate(['order']);
        // this.activeModal.dismiss(result);
    };
    OrderDetailComponent.prototype.onSaveError = function (error) {
        try {
            error.json();
        }
        catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    };
    OrderDetailComponent.prototype.load = function (id) {
        var _this = this;
        this.orderService.find(id).subscribe(function (order) {
            _this.order = order;
            console.log('order status: ', order.orderStatus.toString());
            console.log('enum status: ', 0 /* 'WORKING_COPY' */);
            console.log('enum 3', order.orderStatus.constructor.name);
            console.log('order ', order);
            //     console.log('enum 3', ]);
            _this.isReadOnly = order.orderStatus != null && order.orderStatus != 'WORKING_COPY';
        });
    };
    OrderDetailComponent.prototype.ngOnDestroy = function () {
        this.subscription.unsubscribe();
        // this.eventManager.destroy(this.eventSubscriber);
    };
    OrderDetailComponent.prototype.openModal = function (content, row) {
        var _this = this;
        if (!this.order.estimations[row].drawing) {
            var drawing = { id: null, attachments: [] };
            this.order.estimations[row].drawing = drawing;
        }
        this.clickedRow = row;
        this.modalService.open(content, { size: 'lg' }).result.then(function (result) {
            _this.closeResult = "Closed with: " + result;
        }, function (reason) {
            _this.closeResult = "Dismissed " + _this.getDismissReason(reason);
        });
    };
    OrderDetailComponent.prototype.getDismissReason = function (reason) {
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
    OrderDetailComponent.prototype.convertToDate = function (ngBootstrapDate) {
        if (!ngBootstrapDate && ngBootstrapDate !== null) {
            if (typeof ngBootstrapDate === 'string') {
                return null;
                // return new Date( ngBootstrapDate);
            }
            return new Date(ngBootstrapDate.year, ngBootstrapDate.month, ngBootstrapDate.day);
        }
        return null;
    };
    OrderDetailComponent.prototype.isProductionCreateBtnDisabled = function () {
        var isDisabled = false;
        for (var _i = 0, _a = this.order.estimations; _i < _a.length; _i++) {
            var estimation = _a[_i];
            isDisabled = isDisabled || estimation.estimatedCost !== null;
        }
        return isDisabled;
    };
    OrderDetailComponent = __decorate([
        core_1.Component({
            selector: 'tn-order-detail',
            templateUrl: './order-detail.component.html',
            // styles: [],
            styleUrls: ['./order-detail.component.css'],
        })
    ], OrderDetailComponent);
    return OrderDetailComponent;
}());
exports.OrderDetailComponent = OrderDetailComponent;
