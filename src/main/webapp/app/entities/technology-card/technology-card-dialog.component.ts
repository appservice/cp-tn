import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TechnologyCard } from './technology-card.model';
import { TechnologyCardPopupService } from './technology-card-popup.service';
import { TechnologyCardService } from './technology-card.service';
import { Drawing, DrawingService } from '../drawing';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-technology-card-dialog',
    templateUrl: './technology-card-dialog.component.html'
})
export class TechnologyCardDialogComponent implements OnInit {

    technologyCard: TechnologyCard;
    isSaving: boolean;

    drawings: Drawing[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private technologyCardService: TechnologyCardService,
        private drawingService: DrawingService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.drawingService.query()
            .subscribe((res: ResponseWrapper) => { this.drawings = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.technologyCard.id !== undefined) {
            this.subscribeToSaveResponse(
                this.technologyCardService.update(this.technologyCard));
        } else {
            this.subscribeToSaveResponse(
                this.technologyCardService.create(this.technologyCard));
        }
    }

    private subscribeToSaveResponse(result: Observable<TechnologyCard>) {
        result.subscribe((res: TechnologyCard) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TechnologyCard) {
        this.eventManager.broadcast({ name: 'technologyCardListModification', content: 'OK'});
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

    trackDrawingById(index: number, item: Drawing) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-technology-card-popup',
    template: ''
})
export class TechnologyCardPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private technologyCardPopupService: TechnologyCardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.technologyCardPopupService
                    .open(TechnologyCardDialogComponent as Component, params['id']);
            } else {
                this.technologyCardPopupService
                    .open(TechnologyCardDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
