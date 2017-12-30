/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { TnTestModule } from '../../../test.module';
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
                    CooperationService
                ]
            })
            .overrideTemplate(CooperationDetailComponent, '')
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

                spyOn(service, 'find').and.returnValue(Observable.of(new Cooperation(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cooperation).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
