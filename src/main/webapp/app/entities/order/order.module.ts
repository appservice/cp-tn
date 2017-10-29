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
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {OrderToEstimationComponent} from './inquiry/orders-to-estimation/order-to-estimation.component';
import {OrderInEstimationComponent} from './inquiry/orders-in-estimation/order-in-estimation.component';
import {EstimatedOrderComponent} from './inquiry/estimated-order/estimated-order.component';
import {
    MoveToArchiveDialogComponent,
    MoveToArchivePopupComponent
} from './inquiry/estimated-order/move-to-archive-dialog.component';
import {PurchaseOrderComponent} from './purchase-order/purchase-order.component';
import {ArchiveOrdersComponent} from './inquiry/archive-orders/archive-orders.component';
import {OrderSpreedSheetComponent} from './inquiry/order-detail/order-spreed-sheet/order-spreed-sheet.component';
import { NgxMyDatePickerModule } from 'ngx-mydatepicker';
// import {HotTableModule} from 'angular-handsontable';
import {ExcelService} from '../../tn-components/excel.service';
import {MyDatePickerModule} from 'mydatepicker';
import {emergencyOderRoute, EmergencyOrderResolvePagingParams} from './emergency-order/emregency-order.route';
import {NewEmergencyOrderComponent} from './emergency-order/new-order/new-emergency-order.component';
import {EmergencyOrderComponent} from './emergency-order/emergency-order.component';
import {EmergencyOrderDetailComponent} from './emergency-order/emergency-order-detail/emergency-order-detail.component';
import {EmergencyOrderItemFinderComponent} from './emergency-order/emergency-order-item-finder/emergency_order-item-finder.component';
import {PurchaseOrderItemFinderComponent} from './purchase-order/purchase-order-item-finder/purchase-order-item-finder.component';
import {PurchaseOrderDetailComponent} from './purchase-order/purchase-order-detail/purchase-order-detail.component';
import {NewPurchaseOrderComponent} from './purchase-order/new-purchase-order/new-purchase-order.component';
import {InquiryItemFinderComponent} from "./inquiry/inquiry-item-finder/inquiry-item-finder.component";
import {PreEstimationComponent} from './inquiry/new-order/pre-estimation/pre-estimation.component';
import {NewOrderComponent} from './inquiry/new-order/new-order.component';


const ENTITY_STATES = [
    ...orderRoute,
    ...orderPopupRoute,
    ...emergencyOderRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true}),
        BrowserAnimationsModule,
        TnComponentsModule,
        NgxMyDatePickerModule,
        MyDatePickerModule,
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
        PurchaseOrderItemFinderComponent,
        NewEmergencyOrderComponent,
        EmergencyOrderComponent,
        EmergencyOrderDetailComponent,
        EmergencyOrderItemFinderComponent,


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
        EmergencyOrderResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnOrderModule {
}
