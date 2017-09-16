import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Unit } from './unit.model';
import { UnitPopupService } from './unit-popup.service';
import { UnitService } from './unit.service';

@Component({
    selector: 'jhi-unit-delete-dialog',
    templateUrl: './unit-delete-dialog.component.html'
})
export class UnitDeleteDialogComponent {

    unit: Unit;

    constructor(
        private unitService: UnitService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.unitService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'unitListModification',
                content: 'Deleted an unit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-unit-delete-popup',
    template: ''
})
export class UnitDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private unitPopupService: UnitPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.unitPopupService
                .open(UnitDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
