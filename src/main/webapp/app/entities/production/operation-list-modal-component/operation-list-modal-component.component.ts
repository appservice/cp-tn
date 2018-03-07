import {Component, OnInit, ViewEncapsulation, OnDestroy} from '@angular/core';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {ActivatedRoute} from '@angular/router';
import {OperationListPopupService} from './operation-list-popup.service';
import {JhiAlertService, JhiEventManager} from "ng-jhipster";
import {OperationReportDTO} from "../../operation/operation-report.model";
import {OperationService} from '../../operation/operation.service';
import {Observable} from "rxjs/Rx";

@Component({
    selector: 'tn-operation-list-modal-component',
    templateUrl: './operation-list-modal-component.component.html',
    encapsulation: ViewEncapsulation.None,
    styles: [],

})
export class OperationListModalComponent implements OnInit {

    closeResult: string;
    isSaving: boolean = false;
    operationsReportList: OperationReportDTO [];

    constructor(private alertService: JhiAlertService,
                private operationService: OperationService,
                private eventManager: JhiEventManager,
                public activeModal: NgbActiveModal,) {
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }


    ngOnInit() {
        console.log(this.operationsReportList);
    }


    setAllOperationsFinished() {
        this.isSaving = true;
        if (this.operationsReportList && this.operationsReportList.length > 0) {
            this.operationService.setAllOperationsFinished(this.operationsReportList[0].estimationId).subscribe(
                (res: any) => {
                    this.onSaveSuccess();
                },
                (error: any) => {
                    this.onSaveError(error);
                }
            )
        }
    }

    save() {
        this.isSaving = true;


        this.operationService.updateOperationReportsStatus(this.operationsReportList).subscribe(
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
    selector: 'tn-operation-list-modal-popup',
    template: ''
})
export class OperationListPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private operationListPopupService: OperationListPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.operationListPopupService
                    .open(OperationListModalComponent as Component, params['id']);
            } else {
                this.operationListPopupService
                    .open(OperationListModalComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
