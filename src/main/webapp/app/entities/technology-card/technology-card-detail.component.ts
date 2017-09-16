import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TechnologyCard } from './technology-card.model';
import { TechnologyCardService } from './technology-card.service';

@Component({
    selector: 'jhi-technology-card-detail',
    templateUrl: './technology-card-detail.component.html'
})
export class TechnologyCardDetailComponent implements OnInit, OnDestroy {

    technologyCard: TechnologyCard;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private technologyCardService: TechnologyCardService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTechnologyCards();
    }

    load(id) {
        this.technologyCardService.find(id).subscribe((technologyCard) => {
            this.technologyCard = technologyCard;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTechnologyCards() {
        this.eventSubscriber = this.eventManager.subscribe(
            'technologyCardListModification',
            (response) => this.load(this.technologyCard.id)
        );
    }
}
