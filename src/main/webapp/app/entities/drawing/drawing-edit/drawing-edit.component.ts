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
                private router:Router) {
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

    save() {
        console.log('save is clicked');
        console.log(this.drawing);
        this.isSaving=true;
        this.drawingService.update(this.drawing).subscribe((drawing: Drawing) => {
            console.log("updated: ", drawing);
            this.onSaveSuccess(drawing);

        }, (error: any) => {
            console.log("error occured");
            this.onSaveError(error);
        });
    }


    private onSaveSuccess(result: Drawing) {
        this.eventManager.broadcast({ name: 'drawingListModification', content: 'OK'});
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
