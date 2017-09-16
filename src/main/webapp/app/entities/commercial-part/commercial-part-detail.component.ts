import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { CommercialPart } from './commercial-part.model';
import { CommercialPartService } from './commercial-part.service';

@Component({
    selector: 'jhi-commercial-part-detail',
    templateUrl: './commercial-part-detail.component.html'
})
export class CommercialPartDetailComponent implements OnInit, OnDestroy {

    commercialPart: CommercialPart;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private commercialPartService: CommercialPartService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCommercialParts();
    }

    load(id) {
        this.commercialPartService.find(id).subscribe((commercialPart) => {
            this.commercialPart = commercialPart;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCommercialParts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'commercialPartListModification',
            (response) => this.load(this.commercialPart.id)
        );
    }
}
