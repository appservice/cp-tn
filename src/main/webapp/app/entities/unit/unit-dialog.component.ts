import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Unit } from './unit.model';
import { UnitPopupService } from './unit-popup.service';
import { UnitService } from './unit.service';

@Component({
    selector: 'jhi-unit-dialog',
    templateUrl: './unit-dialog.component.html'
})
export class UnitDialogComponent implements OnInit {

    unit: Unit;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private unitService: UnitService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.unit.id !== undefined) {
            this.subscribeToSaveResponse(
                this.unitService.update(this.unit));
        } else {
            this.subscribeToSaveResponse(
                this.unitService.create(this.unit));
        }
    }

    private subscribeToSaveResponse(result: Observable<Unit>) {
        result.subscribe((res: Unit) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Unit) {
        this.eventManager.broadcast({ name: 'unitListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-unit-popup',
    template: ''
})
export class UnitPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private unitPopupService: UnitPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.unitPopupService
                    .open(UnitDialogComponent as Component, params['id']);
            } else {
                this.unitPopupService
                    .open(UnitDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
