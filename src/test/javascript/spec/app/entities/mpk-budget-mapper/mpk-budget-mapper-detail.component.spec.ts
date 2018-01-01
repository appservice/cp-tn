/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { TnTestModule } from '../../../test.module';
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
                    MpkBudgetMapperService
                ]
            })
            .overrideTemplate(MpkBudgetMapperDetailComponent, '')
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

                spyOn(service, 'find').and.returnValue(Observable.of(new MpkBudgetMapper(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.mpkBudgetMapper).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
