import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TechnologyCard } from './technology-card.model';
import { TechnologyCardPopupService } from './technology-card-popup.service';
import { TechnologyCardService } from './technology-card.service';

@Component({
    selector: 'jhi-technology-card-delete-dialog',
    templateUrl: './technology-card-delete-dialog.component.html'
})
export class TechnologyCardDeleteDialogComponent {

    technologyCard: TechnologyCard;

    constructor(
        private technologyCardService: TechnologyCardService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.technologyCardService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'technologyCardListModification',
                content: 'Deleted an technologyCard'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-technology-card-delete-popup',
    template: ''
})
export class TechnologyCardDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private technologyCardPopupService: TechnologyCardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.technologyCardPopupService
                .open(TechnologyCardDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
