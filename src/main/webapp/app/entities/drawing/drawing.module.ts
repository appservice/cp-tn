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
import {DrawingEditComponent} from './drawing-edit/drawing-edit.component';
import {DrawingFinderComponent} from './drawing-finder/drawing-finder.component';

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
        DrawingEditComponent,
        DrawingFinderComponent,

    ],
    entryComponents: [
        DrawingComponent,
        DrawingDialogComponent,
        DrawingPopupComponent,
        DrawingDeleteDialogComponent,
        DrawingDeletePopupComponent,
        DrawingFinderComponent,
    ],
    providers: [
        DrawingService,
        DrawingPopupService,
        DrawingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnDrawingModule {}
