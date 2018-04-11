import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';
import {Headers, Http, RequestOptions, Response, ResponseContentType} from '@angular/http';
import {OperatorService} from '../entities/operator';
import * as FileSaver from 'file-saver';


@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    isButtonDisabled:Boolean=false;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private http:Http,
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }



    downloadFile(): void {
        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({responseType: ResponseContentType.Blob, headers});

        this.isButtonDisabled=true;
        this.http.get("/api/fx-client/client-resource/file", options)

            .map((res: Response) => res.blob())
            .subscribe((data: any) => {
                OperatorService.saveDownload(data, "Aplikacja.zip", 'application/zip');
                this.isButtonDisabled=false;

            },error1 => {
                console.log("Error: ",error1);
                this.isButtonDisabled=false;
            });
    }

    static saveDownload(responseData: any, fileName: string, contentType: string) {
        const data: Blob = new Blob([responseData], {type: contentType});

        const disableAutoBOM = true;

        FileSaver.saveAs(data,  fileName , disableAutoBOM);
    }
}
