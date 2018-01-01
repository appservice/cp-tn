import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Cooperation } from './cooperation.model';
import { CooperationService } from './cooperation.service';

@Component({
    selector: 'jhi-cooperation-detail',
    templateUrl: './cooperation-detail.component.html'
})
export class CooperationDetailComponent implements OnInit, OnDestroy {

    cooperation: Cooperation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cooperationService: CooperationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCooperation();
    }

    load(id) {
        this.cooperationService.find(id).subscribe((cooperation) => {
            this.cooperation = cooperation;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCooperation() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cooperationListModification',
            (response) => this.load(this.cooperation.id)
        );
    }
}
