import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { TechnologyCard } from './technology-card.model';
import { TechnologyCardService } from './technology-card.service';

@Injectable()
export class TechnologyCardPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private technologyCardService: TechnologyCardService

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
                this.technologyCardService.find(id).subscribe((technologyCard) => {
                    technologyCard.createdAt = this.datePipe
                        .transform(technologyCard.createdAt, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.technologyCardModalRef(component, technologyCard);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.technologyCardModalRef(component, new TechnologyCard());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    technologyCardModalRef(component: Component, technologyCard: TechnologyCard): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.technologyCard = technologyCard;
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
