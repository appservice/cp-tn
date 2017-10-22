import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
import {
    MachineService,
    MachinePopupService,
    MachineComponent,
    MachineDetailComponent,
    MachineDialogComponent,
    MachinePopupComponent,
    MachineDeletePopupComponent,
    MachineDeleteDialogComponent,
    machineRoute,
    machinePopupRoute,
    MachineResolvePagingParams,
} from './';
import {MyDatePickerModule} from 'mydatepicker';

const ENTITY_STATES = [
    ...machineRoute,
    ...machinePopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        MyDatePickerModule,
    ],
    declarations: [
        MachineComponent,
        MachineDetailComponent,
        MachineDialogComponent,
        MachineDeleteDialogComponent,
        MachinePopupComponent,
        MachineDeletePopupComponent,
    ],
    entryComponents: [
        MachineComponent,
        MachineDialogComponent,
        MachinePopupComponent,
        MachineDeleteDialogComponent,
        MachineDeletePopupComponent,
    ],
    providers: [
        MachineService,
        MachinePopupService,
        MachineResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnMachineModule {}
