"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var OrderSpreedSheetComponent = (function () {
    function OrderSpreedSheetComponent() {
    }
    OrderSpreedSheetComponent.prototype.ngOnInit = function () {
        this.colHeaders = ['Nazwa', 'Nr rys.', 'Ilość', 'Jedn', 'Prop. data realiz.', 'Cena', 'Data realizaciji'];
        this.columns = [
            // {
            //     data: 'id',
            //     type: 'numeric',
            //     width: 40
            // },
            // {
            //     data: 'flag',
            //     renderer: flagRenderer
            // },
            {
                data: 'itemName',
                type: 'text'
            },
            {
                data: 'itemNumber',
                type: 'text'
            },
            {
                data: 'amount',
                type: 'numeric',
            },
            {
                data: 'units',
                type: 'text'
            },
            {
                data: 'nededRealizationDate',
                type: 'date',
                dateFormat: 'MM-DD-YYYY',
                correctFormat: true,
            },
            {
                data: 'estimatedCost',
                type: 'numeric',
                format: '0.00'
            },
            {
                data: 'estimatedRealizationDate',
                type: 'date',
                dateFormat: 'MM-DD-YYYY',
                correctFormat: true,
            }
        ];
    };
    __decorate([
        core_1.Input()
    ], OrderSpreedSheetComponent.prototype, "estimations", void 0);
    OrderSpreedSheetComponent = __decorate([
        core_1.Component({
            selector: 'tn-order-spreed-sheet',
            templateUrl: './order-spreed-sheet.component.html',
            styles: []
        })
    ], OrderSpreedSheetComponent);
    return OrderSpreedSheetComponent;
}());
exports.OrderSpreedSheetComponent = OrderSpreedSheetComponent;
