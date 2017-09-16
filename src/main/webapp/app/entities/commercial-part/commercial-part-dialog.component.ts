import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CommercialPart } from './commercial-part.model';
import { CommercialPartPopupService } from './commercial-part-popup.service';
import { CommercialPartService } from './commercial-part.service';
import { Unit, UnitService } from '../unit';
import { Estimation, EstimationService } from '../estimation';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-commercial-part-dialog',
    templateUrl: './commercial-part-dialog.component.html'
})
export class CommercialPartDialogComponent implements OnInit {

    commercialPart: CommercialPart;
    isSaving: boolean;

    units: Unit[];

    estimations: Estimation[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private commercialPartService: CommercialPartService,
        private unitService: UnitService,
        private estimationService: EstimationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.unitService.query()
            .subscribe((res: ResponseWrapper) => { this.units = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.estimationService.query()
            .subscribe((res: ResponseWrapper) => { this.estimations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.commercialPart.id !== undefined) {
            this.subscribeToSaveResponse(
                this.commercialPartService.update(this.commercialPart));
        } else {
            this.subscribeToSaveResponse(
                this.commercialPartService.create(this.commercialPart));
        }
    }

    private subscribeToSaveResponse(result: Observable<CommercialPart>) {
        result.subscribe((res: CommercialPart) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CommercialPart) {
        this.eventManager.broadcast({ name: 'commercialPartListModification', content: 'OK'});
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

    trackUnitById(index: number, item: Unit) {
        return item.id;
    }

    trackEstimationById(index: number, item: Estimation) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-commercial-part-popup',
    template: ''
})
export class CommercialPartPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private commercialPartPopupService: CommercialPartPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.commercialPartPopupService
                    .open(CommercialPartDialogComponent as Component, params['id']);
            } else {
                this.commercialPartPopupService
                    .open(CommercialPartDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
