import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cooperation } from './cooperation.model';
import { CooperationPopupService } from './cooperation-popup.service';
import { CooperationService } from './cooperation.service';

@Component({
    selector: 'jhi-cooperation-delete-dialog',
    templateUrl: './cooperation-delete-dialog.component.html'
})
export class CooperationDeleteDialogComponent {

    cooperation: Cooperation;

    constructor(
        private cooperationService: CooperationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cooperationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cooperationListModification',
                content: 'Deleted an cooperation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cooperation-delete-popup',
    template: ''
})
export class CooperationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cooperationPopupService: CooperationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cooperationPopupService
                .open(CooperationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
