/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { TnTestModule } from '../../../test.module';
import { CooperationComponent } from '../../../../../../main/webapp/app/entities/cooperation/cooperation.component';
import { CooperationService } from '../../../../../../main/webapp/app/entities/cooperation/cooperation.service';
import { Cooperation } from '../../../../../../main/webapp/app/entities/cooperation/cooperation.model';

describe('Component Tests', () => {

    describe('Cooperation Management Component', () => {
        let comp: CooperationComponent;
        let fixture: ComponentFixture<CooperationComponent>;
        let service: CooperationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [CooperationComponent],
                providers: [
                    CooperationService
                ]
            })
            .overrideTemplate(CooperationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CooperationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CooperationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Cooperation(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cooperation[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
