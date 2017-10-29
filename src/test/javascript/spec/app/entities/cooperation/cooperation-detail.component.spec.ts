/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TnTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CooperationDetailComponent } from '../../../../../../main/webapp/app/entities/cooperation/cooperation-detail.component';
import { CooperationService } from '../../../../../../main/webapp/app/entities/cooperation/cooperation.service';
import { Cooperation } from '../../../../../../main/webapp/app/entities/cooperation/cooperation.model';

describe('Component Tests', () => {

    describe('Cooperation Management Detail Component', () => {
        let comp: CooperationDetailComponent;
        let fixture: ComponentFixture<CooperationDetailComponent>;
        let service: CooperationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [CooperationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CooperationService,
                    JhiEventManager
                ]
            }).overrideTemplate(CooperationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CooperationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CooperationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Cooperation(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cooperation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
