import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import { Drawing } from './drawing.model';
import { DrawingService } from './drawing.service';

@Component({
    selector: 'jhi-drawing-detail',
    templateUrl: './drawing-detail.component.html',
})
export class DrawingDetailComponent implements OnInit, OnDestroy {

    drawing: Drawing;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private drawingService: DrawingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDrawings();
    }

    load(id) {
        this.drawingService.find(id).subscribe((drawing) => {
            this.drawing = drawing;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDrawings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'drawingListModification',
            (response) => this.load(this.drawing.id)
        );
    }


}
