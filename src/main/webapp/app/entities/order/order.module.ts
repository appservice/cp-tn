import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TnSharedModule} from '../../shared';
import {
    OrderService,
    OrderPopupService,
    OrderComponent,
    OrderDetailComponent,
    OrderDialogComponent,
    OrderPopupComponent,
    OrderDeletePopupComponent,
    OrderDeleteDialogComponent,
    orderRoute,
    orderPopupRoute,
    OrderResolvePagingParams,
} from './';
import {NewOrderComponent} from './new-order/new-order.component';
import {PreEstimationComponent} from './new-order/pre-estimation/pre-estimation.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {OrderToEstimationComponent} from './orders-to-estimation/order-to-estimation.component';
import {OrderInEstimationComponent} from './orders-in-estimation/order-in-estimation.component';
import {EstimatedOrderComponent} from './estimated-order/estimated-order.component';
import {
    MoveToArchiveDialogComponent,
    MoveToArchivePopupComponent
} from './estimated-order/move-to-archive-dialog.component';
import {NewPurchaseOrderComponent} from './new-purchase-order/new-purchase-order.component';
import {PurchaseOrderComponent} from './purchase-order/purchase-order.component';
import {ArchiveOrdersComponent} from './archive-orders/archive-orders.component';
import {PurchaseOrderDetailComponent} from './purchase-order-detail/purchase-order-detail.component';
import {OrderSpreedSheetComponent} from './order-detail/order-spreed-sheet/order-spreed-sheet.component';
// import {HotTableModule} from 'angular-handsontable';
import {ExcelService} from '../../tn-components/excel.service';
import {InquiryItemFinderComponent} from './inquiry-item-finder/inquiry-item-finder.component';


const ENTITY_STATES = [
    ...orderRoute,
    ...orderPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true}),
        BrowserAnimationsModule,
        TnComponentsModule,
        // HotTableModule,

    ],
    declarations: [
        OrderComponent,
        OrderDetailComponent,
        OrderDialogComponent,
        OrderDeleteDialogComponent,
        OrderPopupComponent,
        OrderDeletePopupComponent,
        NewOrderComponent,
        PreEstimationComponent,
        OrderToEstimationComponent,
        OrderInEstimationComponent,
        EstimatedOrderComponent,
        MoveToArchiveDialogComponent,
        MoveToArchivePopupComponent,
        NewPurchaseOrderComponent,
        PurchaseOrderComponent,
        ArchiveOrdersComponent,
        PurchaseOrderDetailComponent,
        OrderSpreedSheetComponent,
        InquiryItemFinderComponent,


    ],
    entryComponents: [
        OrderComponent,
        OrderDialogComponent,
        OrderPopupComponent,
        OrderDeleteDialogComponent,
        OrderDeletePopupComponent,
        MoveToArchivePopupComponent,
        MoveToArchiveDialogComponent,
        PurchaseOrderComponent,
        ArchiveOrdersComponent

    ],
    providers: [
        OrderService,
        OrderPopupService,
        OrderResolvePagingParams,
        ExcelService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnOrderModule {
}
