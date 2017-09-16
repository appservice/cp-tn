import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Drawing } from './drawing.model';
import { DrawingPopupService } from './drawing-popup.service';
import { DrawingService } from './drawing.service';
import { Estimation, EstimationService } from '../estimation';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-drawing-dialog',
    templateUrl: './drawing-dialog.component.html'
})
export class DrawingDialogComponent implements OnInit {

    drawing: Drawing;
    isSaving: boolean;

    estimations: Estimation[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private drawingService: DrawingService,
        private estimationService: EstimationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.estimationService.query()
            .subscribe((res: ResponseWrapper) => { this.estimations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        // return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, drawing, field, isImage) {
        // if (event && event.target.files && event.target.files[0]) {
        //     const file = event.target.files[0];
        //     if (isImage && !/^image\//.test(file.type)) {
        //         return;
        //     }
        //     this.dataUtils.toBase64(file, (base64Data) => {
        //         drawing[field] = base64Data;
        //         drawing[`${field}ContentType`] = file.type;
        //     });
        // }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.drawing.id !== undefined) {
            this.subscribeToSaveResponse(
                this.drawingService.update(this.drawing));
        } else {
            this.subscribeToSaveResponse(
                this.drawingService.create(this.drawing));
        }
    }

    private subscribeToSaveResponse(result: Observable<Drawing>) {
        result.subscribe((res: Drawing) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Drawing) {
        this.eventManager.broadcast({ name: 'drawingListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackEstimationById(index: number, item: Estimation) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-drawing-popup',
    template: ''
})
export class DrawingPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private drawingPopupService: DrawingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.drawingPopupService
                    .open(DrawingDialogComponent as Component, params['id']);
            } else {
                this.drawingPopupService
                    .open(DrawingDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
