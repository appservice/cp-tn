import {Component, OnInit, ViewEncapsulation, OnDestroy} from '@angular/core';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {ActivatedRoute} from '@angular/router';
import {JhiAlertService, JhiEventManager} from "ng-jhipster";
import {Observable} from "rxjs/Rx";
import {ArchiveProdPopupService} from './archive-prod-popup.service';
import {Estimation} from "../estimation.model";
import {ProductionService} from '../production.service';

@Component({
    selector: 'tn-archive-prod-dialog-component',
    templateUrl: './archive-prod-dialog-component.html',
    encapsulation: ViewEncapsulation.None,
    styles: [],

})
export class ArchiveProdDialogComponent implements OnInit {

    closeResult: string;
    isSaving: boolean = false;
    estimation: Estimation;
    receiver: string;

    constructor(private alertService: JhiAlertService,
                private productionService: ProductionService,
                private eventManager: JhiEventManager,
                public activeModal: NgbActiveModal,) {
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }


    ngOnInit() {
    }


    save() {
        this.isSaving = true;


        this.productionService.moveProductionItemToArchive(this.estimation.id, this.receiver).subscribe(
            res => {
                this.onSaveSuccess();
            },
            (error: any) => {
                this.onSaveError(error);
            }
        );


    }

    private onSaveSuccess() {
        this.eventManager.broadcast({name: 'itemsInProductionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss();//result
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

@Component({
    selector: 'tn-archive-prod-dialog-popup',
    template: ''
})
export class ArchiveProdDialogPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private archiveProdPopupService: ArchiveProdPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.archiveProdPopupService
                    .open(ArchiveProdDialogComponent as Component, params['id']);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
