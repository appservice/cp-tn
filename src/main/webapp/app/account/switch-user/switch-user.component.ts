import {Component, OnInit} from '@angular/core';
import {AuthServerProvider} from '../../shared/auth/auth-jwt.service';
import {Principal} from '../../shared/auth/principal.service';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {Http, URLSearchParams} from '@angular/http';
import {Route, Router} from '@angular/router';
import {until} from 'selenium-webdriver';
import elementIsNotSelected = until.elementIsNotSelected;
import {Observable} from 'rxjs/Observable';
import {UserService} from '../../shared/user/user.service';
import {User} from '../../shared/user/user.model';

@Component({
    selector: 'tn-switch-user',
    templateUrl: './switch-user.component.html',
    styles: []
})
export class SwitchUserComponent implements OnInit {

    userToSwitch: User;
    searchingUser: boolean;
    searchField: boolean;

    constructor(private authProvider: AuthServerProvider, private principal: Principal,
                private alertService: JhiAlertService, private http: Http, private router: Router,
                private eventManager: JhiEventManager,
                private userService: UserService,) {


    }

    ngOnInit() {
    }


    hideSearchingWhenUnsubscribed = new Observable(() => () => this.searchingUser = false);

    searchUser = (text$: Observable<string>) =>
        text$
            .debounceTime(300)
            .distinctUntilChanged()
            .do(() => this.searchingUser = true)
            .switchMap((term) =>
                this.userService.searchBySentence(term)
                    .do(() => this.searchField = false)
                    .catch(() => {
                        this.searchField = true;
                        return Observable.of([]);
                    }))
            .do(() => {
                this.searchingUser = false;
            })
            .merge(this.hideSearchingWhenUnsubscribed);
    formatter = (x: User) => x.login;
    resultFormatter = (x: User) => x.login;

    /*
        onSelectItem(event: any) {

            console.log('item ', event.item);
        }

    */

    impersonate() {

        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('username', this.userToSwitch.login);

        console.log(this.userToSwitch.login);
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

