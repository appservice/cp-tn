import {Component, OnInit} from '@angular/core';
import {AuthServerProvider} from '../../shared';
import {Principal} from '../../shared';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {Http, URLSearchParams} from '@angular/http';
import {Route, Router} from '@angular/router';
import {UserService} from '../../shared';
import {User} from '../../shared';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/merge';


const states = ['Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado',
    'Connecticut', 'Delaware', 'District Of Columbia', 'Federated States Of Micronesia', 'Florida', 'Georgia',
    'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine',
    'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana',
    'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
    'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island',
    'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Islands', 'Virginia',
    'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];

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

    search = (text$: Observable<string>) =>
        text$
            .debounceTime(300)
            .distinctUntilChanged()
                .do(() => this.searchingUser = true)
                .switchMap((term) =>
                    this.userService.searchBySentence(term)
                        .do(() => this.searchField = false)
                        .catch(() => {
                            this.searchField = true;
                            return of([]);
                        }))
                .do(() => {
                    this.searchingUser = false;
                })
                .merge(this.hideSearchingWhenUnsubscribed);

    // search = (text$: Observable<string>) =>
    //     text$
    //         .debounceTime(200)
    //         .distinctUntilChanged()
    //         .map(term => term.length < 2 ? []
    //             : states.filter(v => v.toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10));

    formatter = (x: User) => x.login;
    resultFormatter = (x: User) => x.login;


   /*     onSelectItem(event: any) {

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

