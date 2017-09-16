import {Component, OnInit, OnDestroy, Input,} from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import {Order} from '../order.model';
import {OrderService} from '../order.service';
import {OrderPopupService} from '../order-popup.service';



@Component({
    selector: 'tn-move-to-archive-dialog',
    templateUrl: './move-to-archive-dialog.component.html'
})
export class MoveToArchiveDialogComponent {

    @Input() order: Order;

    constructor(
        private orderService: OrderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orderService.moveOrderToArchive(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'orderListModification',
                content: 'Moved to archive'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'tn-move-to-archive-popup',
    template: ''
})
export class MoveToArchivePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private orderPopupService: OrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.orderPopupService
                .open(MoveToArchiveDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
