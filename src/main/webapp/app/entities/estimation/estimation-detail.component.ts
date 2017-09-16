import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Estimation } from './estimation.model';
import { EstimationService } from './estimation.service';

@Component({
    selector: 'jhi-estimation-detail',
    templateUrl: './estimation-detail.component.html'
})
export class EstimationDetailComponent implements OnInit, OnDestroy {

    estimation: Estimation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private estimationService: EstimationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEstimations();
    }

    load(id) {
        this.estimationService.find(id).subscribe((estimation) => {
            this.estimation = estimation;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEstimations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'estimationListModification',
            (response) => this.load(this.estimation.id)
        );
    }
}
