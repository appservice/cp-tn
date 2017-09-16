import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Drawing } from './drawing.model';
import { DrawingPopupService } from './drawing-popup.service';
import { DrawingService } from './drawing.service';

@Component({
    selector: 'jhi-drawing-delete-dialog',
    templateUrl: './drawing-delete-dialog.component.html'
})
export class DrawingDeleteDialogComponent {

    drawing: Drawing;

    constructor(
        private drawingService: DrawingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.drawingService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'drawingListModification',
                content: 'Deleted an drawing'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-drawing-delete-popup',
    template: ''
})
export class DrawingDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private drawingPopupService: DrawingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.drawingPopupService
                .open(DrawingDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
