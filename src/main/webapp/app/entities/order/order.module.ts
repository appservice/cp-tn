import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
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
import { NewOrderComponent } from './new-order/new-order.component';
import { PreEstimationComponent } from './new-order/pre-estimation/pre-estimation.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {OrderToEstimationComponent} from './orders-to-estimation/order-to-estimation.component';
import {OrderInEstimationComponent} from './orders-in-estimation/order-in-estimation.component';
import {EstimatedOrderComponent} from './estimated-order/estimated-order.component';
import {
    MoveToArchiveDialogComponent,
    MoveToArchivePopupComponent
} from './estimated-order/move-to-archive-dialog.component';



const ENTITY_STATES = [
    ...orderRoute,
    ...orderPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        BrowserAnimationsModule,
        TnComponentsModule,

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


    ],
    entryComponents: [
        OrderComponent,
        OrderDialogComponent,
        OrderPopupComponent,
        OrderDeleteDialogComponent,
        OrderDeletePopupComponent,
        MoveToArchivePopupComponent,
        MoveToArchiveDialogComponent,

    ],
    providers: [
        OrderService,
        OrderPopupService,
        OrderResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnOrderModule {}
