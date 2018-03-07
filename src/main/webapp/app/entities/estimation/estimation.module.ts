import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TnSharedModule} from '../../shared';

import {
    EstimationService,
    EstimationPopupService,
    EstimationComponent,
    EstimationDetailComponent,
    EstimationDialogComponent,
    EstimationPopupComponent,
    EstimationDeletePopupComponent,
    EstimationDeleteDialogComponent,
    estimationRoute,
    estimationPopupRoute,
    EstimationResolvePagingParams,
} from './';
import {NewEstimationComponent} from './new-estimation/new-estimation.component';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {CurrencyMaskModule} from 'ng2-currency-mask';
import {TnAlert} from '../../tn-components/tn-alert';
import {ConfirmDeactivateGuard} from './estimation.route';
import {TnModalConfirmComponent} from '../../tn-components/tn-modal-confirm/tn-modal-confirm.component';

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
