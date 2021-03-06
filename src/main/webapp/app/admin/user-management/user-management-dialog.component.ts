import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {UserModalService} from './user-modal.service';
import {JhiLanguageHelper, User, UserService} from '../../shared';
import {ClientService} from '../../entities/client/client.service';
import {Client} from '../../entities/client/client.model';
import {Machine} from '../../entities/machine/machine.model';
import {isNullOrUndefined} from "util";

@Component({
    selector: 'jhi-user-mgmt-dialog',
    templateUrl: './user-management-dialog.component.html'
})
export class UserMgmtDialogComponent implements OnInit {

    user: User;
    languages: any[];
    authorities: any[];
    isSaving: Boolean;
    clients: Client[];

    constructor(
        public activeModal: NgbActiveModal,
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private eventManager: JhiEventManager,
        private clientService: ClientService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.authorities = [];

        this.userService.authorities().subscribe((authorities) => {
            this.authorities = authorities;
        });
        this.clients=[];
        this.clientService.getAllNotPageable().subscribe((responseWrapper) => {
            console.log(responseWrapper.json);
            this.clients = responseWrapper.json;
        });


        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.user.id !== null) {
            this.userService.update(this.user).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.userService.create(this.user).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.eventManager.broadcast({ name: 'userListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    trackByClientId(index: number, item: Client) {
        return index;
    }

    compareClients(c1: Client, c2: Client): boolean {
        if (!isNullOrUndefined(c1) && !isNullOrUndefined(c2)) {
            return c1.id === c2.id;

        }
    }
}

@Component({
    selector: 'jhi-user-dialog',
    template: ''
})
export class UserDialogComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userModalService: UserModalService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['login'] ) {
                this.userModalService.open(UserMgmtDialogComponent as Component, params['login']);
            } else {
                this.userModalService.open(UserMgmtDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }




}
