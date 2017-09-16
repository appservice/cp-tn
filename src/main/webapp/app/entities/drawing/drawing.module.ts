import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
import {
    DrawingService,
    DrawingPopupService,
    DrawingComponent,
    DrawingDetailComponent,
    DrawingDialogComponent,
    DrawingPopupComponent,
    DrawingDeletePopupComponent,
    DrawingDeleteDialogComponent,
    drawingRoute,
    drawingPopupRoute,
    DrawingResolvePagingParams,
} from './';
import {TnComponentsModule} from '../../tn-components/tn-components.module';

const ENTITY_STATES = [
    ...drawingRoute,
    ...drawingPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        TnComponentsModule,
    ],
    declarations: [
        DrawingComponent,
        DrawingDetailComponent,
        DrawingDialogComponent,
        DrawingDeleteDialogComponent,
        DrawingPopupComponent,
        DrawingDeletePopupComponent,
    ],
    entryComponents: [
        DrawingComponent,
        DrawingDialogComponent,
        DrawingPopupComponent,
        DrawingDeleteDialogComponent,
        DrawingDeletePopupComponent,
    ],
    providers: [
        DrawingService,
        DrawingPopupService,
        DrawingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnDrawingModule {}
