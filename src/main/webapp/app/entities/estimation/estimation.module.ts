import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TnSharedModule} from '../../shared';

import {
    EstimationComponent,
    EstimationDeleteDialogComponent,
    EstimationDeletePopupComponent,
    EstimationDetailComponent,
    EstimationDialogComponent,
    EstimationPopupComponent,
    estimationPopupRoute,
    EstimationPopupService,
    EstimationResolvePagingParams,
    estimationRoute,
    EstimationService,
} from './';
import {NewEstimationComponent} from './new-estimation/new-estimation.component';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {CurrencyMaskModule} from 'ng2-currency-mask';
import {TnAlert} from '../../tn-components/tn-alert';
import {ConfirmDeactivateGuard} from './estimation.route';
import {DialogModule} from 'primeng/primeng';

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
        DialogModule,



    ],
    declarations: [
        EstimationComponent,
        EstimationDetailComponent,
        EstimationDialogComponent,
        EstimationDeleteDialogComponent,
        EstimationPopupComponent,
        EstimationDeletePopupComponent,
        NewEstimationComponent,

    ],
    entryComponents: [
        EstimationComponent,
        EstimationDialogComponent,
        EstimationPopupComponent,
        EstimationDeleteDialogComponent,
        EstimationDeletePopupComponent,
        TnAlert,
    ],
    providers: [
        EstimationService,
        EstimationPopupService,
        EstimationResolvePagingParams,
        ConfirmDeactivateGuard,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnEstimationModule {
}
