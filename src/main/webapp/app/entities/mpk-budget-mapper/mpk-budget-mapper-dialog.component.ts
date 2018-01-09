import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MpkBudgetMapper } from './mpk-budget-mapper.model';
import { MpkBudgetMapperPopupService } from './mpk-budget-mapper-popup.service';
import { MpkBudgetMapperService } from './mpk-budget-mapper.service';
import { Client, ClientService } from '../client';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-mpk-budget-mapper-dialog',
    templateUrl: './mpk-budget-mapper-dialog.component.html'
})
export class MpkBudgetMapperDialogComponent implements OnInit {

    mpkBudgetMapper: MpkBudgetMapper;
    isSaving: boolean;

    clients: Client[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private mpkBudgetMapperService: MpkBudgetMapperService,
        private clientService: ClientService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.clientService.getAllNotPageable()
            .subscribe((res: ResponseWrapper) => { this.clients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.mpkBudgetMapper.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mpkBudgetMapperService.update(this.mpkBudgetMapper));
        } else {
            this.subscribeToSaveResponse(
                this.mpkBudgetMapperService.create(this.mpkBudgetMapper));
        }
    }

    private subscribeToSaveResponse(result: Observable<MpkBudgetMapper>) {
        result.subscribe((res: MpkBudgetMapper) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: MpkBudgetMapper) {
        this.eventManager.broadcast({ name: 'mpkBudgetMapperListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackClientById(index: number, item: Client) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-mpk-budget-mapper-popup',
    template: ''
})
export class MpkBudgetMapperPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mpkBudgetMapperPopupService: MpkBudgetMapperPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mpkBudgetMapperPopupService
                    .open(MpkBudgetMapperDialogComponent as Component, params['id']);
            } else {
                this.mpkBudgetMapperPopupService
                    .open(MpkBudgetMapperDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
