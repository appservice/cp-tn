import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Operator } from './operator.model';
import { OperatorPopupService } from './operator-popup.service';
import { OperatorService } from './operator.service';

@Component({
    selector: 'jhi-operator-dialog',
    templateUrl: './operator-dialog.component.html'
})
export class OperatorDialogComponent implements OnInit {

    operator: Operator;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private operatorService: OperatorService,
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
        if (this.operator.id !== undefined) {
            this.subscribeToSaveResponse(
                this.operatorService.update(this.operator));
        } else {
            this.subscribeToSaveResponse(
                this.operatorService.create(this.operator));
        }
    }

    private subscribeToSaveResponse(result: Observable<Operator>) {
        result.subscribe((res: Operator) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Operator) {
        this.eventManager.broadcast({ name: 'operatorListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-operator-popup',
    template: ''
})
export class OperatorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private operatorPopupService: OperatorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.operatorPopupService
                    .open(OperatorDialogComponent as Component, params['id']);
            } else {
                this.operatorPopupService
                    .open(OperatorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
