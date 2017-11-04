import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TnSharedModule} from '../../shared';

import {
    EstimationService,

    productionRoute,
    estimationPopupRoute,
    EstimationResolvePagingParams,
} from './';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {CurrencyMaskModule} from 'ng2-currency-mask';
import {ProductionStanComponent} from './production.component';
import {OrdersInProductionComponent} from './orders-in-production/orders-in-production.component';
import {OrderInProductionDetailComponent} from './order-in-production-detail/order-in-production-detail.component';
import {TechnologyEditComponent} from './technology-edit/technology-edit.component';
import {ProductionService} from './production.service';

const ENTITY_STATES = [
    ...productionRoute,
    ...estimationPopupRoute,
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


    ],
    entryComponents: [
        ProductionStanComponent,
        OrdersInProductionComponent,


    ],
    providers: [
        EstimationService,
        ProductionService

        ,
        EstimationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnProductionModule {
}
