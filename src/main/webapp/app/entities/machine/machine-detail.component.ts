import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Machine } from './machine.model';
import { MachineService } from './machine.service';

@Component({
    selector: 'jhi-machine-detail',
    templateUrl: './machine-detail.component.html'
})
export class MachineDetailComponent implements OnInit, OnDestroy {

    machine: Machine;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private machineService: MachineService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMachines();
    }

    load(id) {
        this.machineService.find(id).subscribe((machine) => {
            this.machine = machine;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMachines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'machineListModification',
            (response) => this.load(this.machine.id)
        );
    }
}
