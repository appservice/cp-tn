import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Operator } from './operator.model';
import { OperatorService } from './operator.service';

@Component({
    selector: 'jhi-operator-detail',
    templateUrl: './operator-detail.component.html'
})
export class OperatorDetailComponent implements OnInit, OnDestroy {

    operator: Operator;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private operatorService: OperatorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOperators();
    }

    load(id) {
        this.operatorService.find(id).subscribe((operator) => {
            this.operator = operator;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOperators() {
        this.eventSubscriber = this.eventManager.subscribe(
            'operatorListModification',
            (response) => this.load(this.operator.id)
        );
    }
}
