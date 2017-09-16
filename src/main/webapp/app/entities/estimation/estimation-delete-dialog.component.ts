import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Estimation } from './estimation.model';
import { EstimationPopupService } from './estimation-popup.service';
import { EstimationService } from './estimation.service';

@Component({
    selector: 'jhi-estimation-delete-dialog',
    templateUrl: './estimation-delete-dialog.component.html'
})
export class EstimationDeleteDialogComponent {

    estimation: Estimation;

    constructor(
        private estimationService: EstimationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.estimationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'estimationListModification',
                content: 'Deleted an estimation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-estimation-delete-popup',
    template: ''
})
export class EstimationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private estimationPopupService: EstimationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.estimationPopupService
                .open(EstimationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
