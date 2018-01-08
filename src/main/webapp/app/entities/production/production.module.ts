import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TnSharedModule} from '../../shared';

import {EstimationService, ItemsInProductionPagingParams, OperationListPopupComponent, productionPopupRoute, productionRoute,} from './';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {CurrencyMaskModule} from 'ng2-currency-mask';
import {ProductionStanComponent} from './production.component';
import {OrdersInProductionComponent} from './orders-in-production/orders-in-production.component';
import {OrderInProductionDetailComponent} from './order-in-production-detail/order-in-production-detail.component';
import {TechnologyEditComponent} from './technology-edit/technology-edit.component';
import {ProductionService} from './production.service';
import {EmergencyOrderTechnologyEditComponent} from './emergency-order-technology-edit/emergency-order-technology-edit.component';
import {OperationListModalComponent} from "./operation-list-modal-component/operation-list-modal-component.component";
import {OperationListPopupService} from "./operation-list-modal-component/operation-list-popup.service";
import {ArchiveProdDialogComponent, ArchiveProdDialogPopupComponent} from './archive-prod-dialog/archive-prod-dialog.component';
import {ArchiveProdPopupService} from './archive-prod-dialog/archive-prod-popup.service';
import {FinishedDetailsComponent} from './finished-details-component/finished-details.component';
import {FinishedItemsPagingParams} from './production.route';

const ENTITY_STATES = [
    ...productionRoute,
    ...productionPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true}),
        TnComponentsModule,
        CurrencyMaskModule,


    ],
    declarations: [
        ProductionStanComponent,
        OrdersInProductionComponent,
        OrderInProductionDetailComponent,
        TechnologyEditComponent,
        EmergencyOrderTechnologyEditComponent,
        OperationListModalComponent,
        OperationListPopupComponent,

        ArchiveProdDialogComponent,
        ArchiveProdDialogPopupComponent,
        FinishedDetailsComponent,


    ],
    entryComponents: [
        ProductionStanComponent,
        OrdersInProductionComponent,
        OperationListModalComponent,
        OperationListPopupComponent,

        ArchiveProdDialogComponent,
        ArchiveProdDialogPopupComponent,


    ],
    providers: [
        EstimationService,
        ProductionService,
        OperationListPopupService,
        ItemsInProductionPagingParams,
        ArchiveProdPopupService,
        FinishedItemsPagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnProductionModule {
}
