import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {Estimation} from "../../estimation/estimation.model";
import {EstimationService} from '../../estimation/estimation.service';
import {OperationService} from '../../operation/operation.service';
import {OperationReportDTO} from '../../operation/operation-report.model';
import {ResponseWrapper} from "../../../shared/index";


@Injectable()
export class OperationListPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private estimationService: EstimationService,
        private operationService: OperationService,

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.operationService.getOperationsReport(id).subscribe((res: ResponseWrapper) => {
                    this.ngbModalRef = this.clientModalRef(component, res.json);
                    resolve(this.ngbModalRef);
                });
            };
        });
    }

    clientModalRef(component: Component, operationsReportList: any): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static',windowClass: 'modal-xxl'});//,windowClass: 'operations-list-modal'
        modalRef.componentInstance.operationsReportList = operationsReportList;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
