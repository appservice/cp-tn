import {Component, OnDestroy, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiLanguageService} from 'ng-jhipster';

import {ProfileService} from '../profiles/profile.service';
import {JhiLanguageHelper, Principal, LoginModalService, LoginService} from '../../shared';

import {VERSION, DEBUG_INFO_ENABLED} from '../../app.constants';
import {Subscription} from 'rxjs/Subscription';
import { Http, Response } from '@angular/http';



@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: [
        'navbar.scss'
    ]
})
export class NavbarComponent implements OnInit, OnDestroy {

    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    userName: string = null;
    eventSubscriber: Subscription;

    constructor(private loginService: LoginService,
                private languageService: JhiLanguageService,
                private languageHelper: JhiLanguageHelper,
                private principal: Principal,
                private loginModalService: LoginModalService,
                private profileService: ProfileService,
                private router: Router,
                private eventManager: JhiEventManager,
                private http: Http) {
        this
            .version = VERSION ? 'v' + VERSION : '';
        this
            .isNavbarCollapsed = true;
    }

    ngOnInit() {
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });

        this.profileService.getProfileInfo().then((profileInfo) => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });


        this.principal.identity(false).then((response) => {
                if (response !== null) {
                    this.userName = response.login;

                }
            }
        );
        this.registerUserNameChangeListener();
    }

    changeLanguage(languageKey: string) {
        this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
        this.eventManager.broadcast({name: 'userNameModification', content: null});

    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }

    registerUserNameChangeListener() {
        this.eventSubscriber = this.eventManager.subscribe('userNameModification',
            (response) => {
                if (response.content != null) {
                    this.userName = response.content.login;

                } else {
                    this.userName = null;
                }
            });
    }


    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    reindexElasticsearch(): void {
        this.http.post('api/elasticsearch/index',null,null).subscribe((resp:Response)=>{
            console.log(resp);
        },(error: any)=>console.log(error));
    }

    clearCache():void {
        this.http.get('api/cache/clear',null).subscribe((resp:Response)=>{
            console.log('Cleared cache. ',resp)
        },(error: any)=>console.log(error));
    }
}
