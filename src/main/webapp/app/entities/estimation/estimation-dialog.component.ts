import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Estimation } from './estimation.model';
import { EstimationPopupService } from './estimation-popup.service';
import { EstimationService } from './estimation.service';
import { Order, OrderService } from '../order';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-estimation-dialog',
    templateUrl: './estimation-dialog.component.html'
})
export class EstimationDialogComponent implements OnInit {

    estimation: Estimation;
    isSaving: boolean;

    orders: Order[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private estimationService: EstimationService,
        private orderService: OrderService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.orderService.query()
            .subscribe((res: ResponseWrapper) => { this.orders = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.estimation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.estimationService.update(this.estimation));
        } else {
            this.subscribeToSaveResponse(
                this.estimationService.create(this.estimation));
        }
    }

    private subscribeToSaveResponse(result: Observable<Estimation>) {
        result.subscribe((res: Estimation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Estimation) {
        this.eventManager.broadcast({ name: 'estimationListModification', content: 'OK'});
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

    trackOrderById(index: number, item: Order) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-estimation-popup',
    template: ''
})
export class EstimationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private estimationPopupService: EstimationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.estimationPopupService
                    .open(EstimationDialogComponent as Component, params['id']);
            } else {
                this.estimationPopupService
                    .open(EstimationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
