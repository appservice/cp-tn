import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {Estimation} from "../../estimation/estimation.model";
import {EstimationService} from '../../estimation/estimation.service';


@Injectable()
export class OperationListPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private estimationService: EstimationService

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
                this.estimationService.find(id).subscribe((client) => {
                    this.ngbModalRef = this.clientModalRef(component, client);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.clientModalRef(component, new Estimation());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    clientModalRef(component: Component, client: Estimation): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static',windowClass: 'operations-list-modal' });
        modalRef.componentInstance.client = client;
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
