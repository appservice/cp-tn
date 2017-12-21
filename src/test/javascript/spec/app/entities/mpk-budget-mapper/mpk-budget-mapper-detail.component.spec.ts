/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TnTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MpkBudgetMapperDetailComponent } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper-detail.component';
import { MpkBudgetMapperService } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper.service';
import { MpkBudgetMapper } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper.model';

describe('Component Tests', () => {

    describe('MpkBudgetMapper Management Detail Component', () => {
        let comp: MpkBudgetMapperDetailComponent;
        let fixture: ComponentFixture<MpkBudgetMapperDetailComponent>;
        let service: MpkBudgetMapperService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [MpkBudgetMapperDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MpkBudgetMapperService,
                    JhiEventManager
                ]
            }).overrideTemplate(MpkBudgetMapperDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MpkBudgetMapperDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MpkBudgetMapperService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MpkBudgetMapper(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.mpkBudgetMapper).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
