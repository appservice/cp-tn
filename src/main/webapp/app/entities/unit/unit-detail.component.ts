import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Unit } from './unit.model';
import { UnitService } from './unit.service';

@Component({
    selector: 'jhi-unit-detail',
    templateUrl: './unit-detail.component.html'
})
export class UnitDetailComponent implements OnInit, OnDestroy {

    unit: Unit;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private unitService: UnitService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUnits();
    }

    load(id) {
        this.unitService.find(id).subscribe((unit) => {
            this.unit = unit;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUnits() {
        this.eventSubscriber = this.eventManager.subscribe(
            'unitListModification',
            (response) => this.load(this.unit.id)
        );
    }
}
