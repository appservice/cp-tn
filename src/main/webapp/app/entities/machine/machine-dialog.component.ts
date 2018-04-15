import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService, JhiDateUtils} from 'ng-jhipster';

import {Machine} from './machine.model';
import {MachinePopupService} from './machine-popup.service';
import {MachineService} from './machine.service';
import {Operation, OperationService} from '../operation';
import {Globals, ResponseWrapper} from '../../shared';
import {IMyDpOptions} from 'mydatepicker';
import {IMyDateModel} from 'ngx-mydatepicker';

@Component({
    selector: 'jhi-machine-dialog',
    templateUrl: './machine-dialog.component.html'
})
export class MachineDialogComponent implements OnInit {

    machine: Machine;
    isSaving: boolean;
    validFromDate: any;



    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private machineService: MachineService,
        private operationService: OperationService,
        private eventManager: JhiEventManager,
        private dateUtils: JhiDateUtils,
        public globals: Globals,
    ) {

    }

    ngOnInit() {
        this.isSaving = false;
        this.validFromDate = {date: this.machine.validFrom};


    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.machine.id !== undefined) {
            this.machine.validFrom = this.dateUtils.convertLocalDateToServer(this.validFromDate.date, 'yyyy-MM-dd');
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
        this.eventManager.broadcast({name: 'machineListModification', content: 'OK'});
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
    ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
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
