import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Machine } from './machine.model';
import { MachineService } from './machine.service';
import {IMyDpOptions} from 'mydatepicker';
import {MachineDtl} from './machineDtl.model';
import {ResponseWrapper} from '../../shared/model/response-wrapper.model';

@Component({
    selector: 'jhi-machine-detail',
    templateUrl: './machine-detail.component.html'
})
export class MachineDetailComponent implements OnInit, OnDestroy {

    machine: Machine;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    private machineDtls : MachineDtl[];

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
        this.machineService.getMachineDtlByMachineId(id).subscribe((res: ResponseWrapper) => {
            this.machineDtls = res.json;
        }, (error) => console.log(error));
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
