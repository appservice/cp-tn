/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { TnTestModule } from '../../../test.module';
import { MpkBudgetMapperComponent } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper.component';
import { MpkBudgetMapperService } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper.service';
import { MpkBudgetMapper } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper.model';

describe('Component Tests', () => {

    describe('MpkBudgetMapper Management Component', () => {
        let comp: MpkBudgetMapperComponent;
        let fixture: ComponentFixture<MpkBudgetMapperComponent>;
        let service: MpkBudgetMapperService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [MpkBudgetMapperComponent],
                providers: [
                    MpkBudgetMapperService
                ]
            })
            .overrideTemplate(MpkBudgetMapperComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MpkBudgetMapperComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MpkBudgetMapperService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new MpkBudgetMapper(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.mpkBudgetMappers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
