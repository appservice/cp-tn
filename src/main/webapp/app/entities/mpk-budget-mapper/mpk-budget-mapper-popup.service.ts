import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MpkBudgetMapper } from './mpk-budget-mapper.model';
import { MpkBudgetMapperService } from './mpk-budget-mapper.service';

@Injectable()
export class MpkBudgetMapperPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private mpkBudgetMapperService: MpkBudgetMapperService

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
                this.mpkBudgetMapperService.find(id).subscribe((mpkBudgetMapper) => {
                    this.ngbModalRef = this.mpkBudgetMapperModalRef(component, mpkBudgetMapper);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.mpkBudgetMapperModalRef(component, new MpkBudgetMapper());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    mpkBudgetMapperModalRef(component: Component, mpkBudgetMapper: MpkBudgetMapper): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.mpkBudgetMapper = mpkBudgetMapper;
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
