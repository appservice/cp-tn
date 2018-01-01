import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Operator } from './operator.model';
import { OperatorPopupService } from './operator-popup.service';
import { OperatorService } from './operator.service';

@Component({
    selector: 'jhi-operator-delete-dialog',
    templateUrl: './operator-delete-dialog.component.html'
})
export class OperatorDeleteDialogComponent {

    operator: Operator;

    constructor(
        private operatorService: OperatorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.operatorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'operatorListModification',
                content: 'Deleted an operator'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-operator-delete-popup',
    template: ''
})
export class OperatorDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private operatorPopupService: OperatorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.operatorPopupService
                .open(OperatorDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
