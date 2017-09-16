import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CommercialPart } from './commercial-part.model';
import { CommercialPartPopupService } from './commercial-part-popup.service';
import { CommercialPartService } from './commercial-part.service';

@Component({
    selector: 'jhi-commercial-part-delete-dialog',
    templateUrl: './commercial-part-delete-dialog.component.html'
})
export class CommercialPartDeleteDialogComponent {

    commercialPart: CommercialPart;

    constructor(
        private commercialPartService: CommercialPartService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.commercialPartService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'commercialPartListModification',
                content: 'Deleted an commercialPart'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-commercial-part-delete-popup',
    template: ''
})
export class CommercialPartDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private commercialPartPopupService: CommercialPartPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.commercialPartPopupService
                .open(CommercialPartDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
