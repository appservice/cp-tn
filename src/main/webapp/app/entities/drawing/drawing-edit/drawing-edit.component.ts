import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiDataUtils, JhiAlertService} from 'ng-jhipster';

import {Drawing} from '../drawing.model';
import {DrawingService} from '../drawing.service';

@Component({
    selector: 'tn-drawing-edit',
    templateUrl: './drawing-edit.component.html',
})
export class DrawingEditComponent implements OnInit, OnDestroy {

    drawing: Drawing;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    private isSaving: boolean;

    constructor(private eventManager: JhiEventManager,
                private dataUtils: JhiDataUtils,
                private drawingService: DrawingService,
                private route: ActivatedRoute,
                private alertService: JhiAlertService,
                private router: Router) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.load(params['id']);

            } else {
                this.drawing = new Drawing();
                this.drawing.attachments = [];
            }
        });
        this.registerChangeInDrawings();
    }

    load(id) {
        this.drawingService.find(id).subscribe((drawing) => {
            this.drawing = drawing;
        });
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

    save() {
        console.log('save is clicked');
        console.log(this.drawing);
        for (let attachment of this.drawing.attachments) {
            attachment.drawingId = this.drawing.id;
        }

        this.isSaving = true;

        if (this.drawing.id !== undefined && this.drawing.id !== null) {

            this.drawingService.update(this.drawing).subscribe((drawing: Drawing) => {
                console.log('updated: ', drawing);
                this.onSaveSuccess(drawing);

            }, (error: any) => {
                console.log('error occured');
                this.onSaveError(error);
            });
        } else {
            this.drawingService.create(this.drawing).subscribe((drawing: Drawing) => {
                console.log('updated: ', drawing);
                this.onSaveSuccess(drawing);

            }, (error: any) => {
                console.log('error occured');
                this.onSaveError(error);
            });
        }
    }

    private onSaveSuccess(result: Drawing) {
        this.eventManager.broadcast({name: 'drawingListModification', content: 'OK'});
        this.isSaving = false;
        this.router.navigate(['drawing']);

    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

}
