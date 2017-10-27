import {Component, OnInit} from '@angular/core';
import {AuthServerProvider} from '../../shared/auth/auth-jwt.service';
import {Principal} from '../../shared/auth/principal.service';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {Http, URLSearchParams} from '@angular/http';
import {Route, Router} from '@angular/router';

@Component({
    selector: 'tn-switch-user',
    templateUrl: './switch-user.component.html',
    styles: []
})
export class SwitchUserComponent implements OnInit {

    impersonateLogin: string;

    constructor(private authProvider: AuthServerProvider, private principal: Principal,
                private alertService: JhiAlertService, private http: Http, private router: Router,
                private eventManager: JhiEventManager,
    ) {


    }

    ngOnInit() {
    }


    impersonate() {

        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('username', this.impersonateLogin);

        console.log(this.impersonateLogin);
        console.log(urlSearchParams);

        this.http.get('/api/login/impersonate', {params: urlSearchParams}).subscribe(
            (res) => {

                let resp = res.json();
                this.authProvider.logout();

                // Login with new token
                this.authProvider.loginWithToken(resp.id_token, true).then((value) => {
                    this.principal.identity(true).then((value) => {
                        console.log(value);
                        this.eventManager.broadcast({name: 'userNameModification', content: value});
                        this.router.navigateByUrl('/');
                    });
                });


            }
        );
    }

}

