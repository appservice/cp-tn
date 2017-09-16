import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TnSharedModule} from '../../shared';

import {
    EstimationService,

    estimationRoute,
    estimationPopupRoute,
    EstimationResolvePagingParams,
} from './';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {CurrencyMaskModule} from 'ng2-currency-mask';
import {ProductionStanComponent} from './production.component';

const ENTITY_STATES = [
    ...estimationRoute,
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


    ],
    entryComponents: [
        ProductionStanComponent,

    ],
    providers: [
        EstimationService,
        ,
        EstimationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnProductionModule {
}
