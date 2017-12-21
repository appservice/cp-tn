import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MpkBudgetMapper } from './mpk-budget-mapper.model';
import { MpkBudgetMapperPopupService } from './mpk-budget-mapper-popup.service';
import { MpkBudgetMapperService } from './mpk-budget-mapper.service';

@Component({
    selector: 'jhi-mpk-budget-mapper-delete-dialog',
    templateUrl: './mpk-budget-mapper-delete-dialog.component.html'
})
export class MpkBudgetMapperDeleteDialogComponent {

    mpkBudgetMapper: MpkBudgetMapper;

    constructor(
        private mpkBudgetMapperService: MpkBudgetMapperService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mpkBudgetMapperService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'mpkBudgetMapperListModification',
                content: 'Deleted an mpkBudgetMapper'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-mpk-budget-mapper-delete-popup',
    template: ''
})
export class MpkBudgetMapperDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mpkBudgetMapperPopupService: MpkBudgetMapperPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mpkBudgetMapperPopupService
                .open(MpkBudgetMapperDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
