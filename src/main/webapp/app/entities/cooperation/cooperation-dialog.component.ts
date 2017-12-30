import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Cooperation } from './cooperation.model';
import { CooperationPopupService } from './cooperation-popup.service';
import { CooperationService } from './cooperation.service';
import { Estimation, EstimationService } from '../estimation';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-cooperation-dialog',
    templateUrl: './cooperation-dialog.component.html'
})
export class CooperationDialogComponent implements OnInit {

    cooperation: Cooperation;
    isSaving: boolean;

    estimations: Estimation[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cooperationService: CooperationService,
        private estimationService: EstimationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.estimationService.query()
            .subscribe((res: ResponseWrapper) => { this.estimations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cooperation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cooperationService.update(this.cooperation));
        } else {
            this.subscribeToSaveResponse(
                this.cooperationService.create(this.cooperation));
        }
    }

    private subscribeToSaveResponse(result: Observable<Cooperation>) {
        result.subscribe((res: Cooperation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Cooperation) {
        this.eventManager.broadcast({ name: 'cooperationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackEstimationById(index: number, item: Estimation) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cooperation-popup',
    template: ''
})
export class CooperationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cooperationPopupService: CooperationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cooperationPopupService
                    .open(CooperationDialogComponent as Component, params['id']);
            } else {
                this.cooperationPopupService
                    .open(CooperationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
