import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Machine } from './machine.model';
import { MachinePopupService } from './machine-popup.service';
import { MachineService } from './machine.service';

@Component({
    selector: 'jhi-machine-delete-dialog',
    templateUrl: './machine-delete-dialog.component.html'
})
export class MachineDeleteDialogComponent {

    machine: Machine;

    constructor(
        private machineService: MachineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.machineService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'machineListModification',
                content: 'Deleted an machine'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-machine-delete-popup',
    template: ''
})
export class MachineDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private machinePopupService: MachinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.machinePopupService
                .open(MachineDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
