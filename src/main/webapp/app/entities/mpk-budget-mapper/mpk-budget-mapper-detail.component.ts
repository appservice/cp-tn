import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { MpkBudgetMapper } from './mpk-budget-mapper.model';
import { MpkBudgetMapperService } from './mpk-budget-mapper.service';

@Component({
    selector: 'jhi-mpk-budget-mapper-detail',
    templateUrl: './mpk-budget-mapper-detail.component.html'
})
export class MpkBudgetMapperDetailComponent implements OnInit, OnDestroy {

    mpkBudgetMapper: MpkBudgetMapper;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private mpkBudgetMapperService: MpkBudgetMapperService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMpkBudgetMappers();
    }

    load(id) {
        this.mpkBudgetMapperService.find(id).subscribe((mpkBudgetMapper) => {
            this.mpkBudgetMapper = mpkBudgetMapper;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMpkBudgetMappers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'mpkBudgetMapperListModification',
            (response) => this.load(this.mpkBudgetMapper.id)
        );
    }
}
