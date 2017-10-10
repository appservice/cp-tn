/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TnTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OperatorDetailComponent } from '../../../../../../main/webapp/app/entities/operator/operator-detail.component';
import { OperatorService } from '../../../../../../main/webapp/app/entities/operator/operator.service';
import { Operator } from '../../../../../../main/webapp/app/entities/operator/operator.model';

describe('Component Tests', () => {

    describe('Operator Management Detail Component', () => {
        let comp: OperatorDetailComponent;
        let fixture: ComponentFixture<OperatorDetailComponent>;
        let service: OperatorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [OperatorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OperatorService,
                    JhiEventManager
                ]
            }).overrideTemplate(OperatorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OperatorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OperatorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Operator(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.operator).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
