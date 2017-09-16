import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Machine } from './machine.model';
import { MachinePopupService } from './machine-popup.service';
import { MachineService } from './machine.service';
import { Operation, OperationService } from '../operation';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-machine-dialog',
    templateUrl: './machine-dialog.component.html'
})
export class MachineDialogComponent implements OnInit {

    machine: Machine;
    isSaving: boolean;

    operations: Operation[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private machineService: MachineService,
        private operationService: OperationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.operationService.query()
            .subscribe((res: ResponseWrapper) => { this.operations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.machine.id !== undefined) {
            this.subscribeToSaveResponse(
                this.machineService.update(this.machine));
        } else {
            this.subscribeToSaveResponse(
                this.machineService.create(this.machine));
        }
    }

    private subscribeToSaveResponse(result: Observable<Machine>) {
        result.subscribe((res: Machine) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Machine) {
        this.eventManager.broadcast({ name: 'machineListModification', content: 'OK'});
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

    trackOperationById(index: number, item: Operation) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-machine-popup',
    template: ''
})
export class MachinePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private machinePopupService: MachinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.machinePopupService
                    .open(MachineDialogComponent as Component, params['id']);
            } else {
                this.machinePopupService
                    .open(MachineDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
