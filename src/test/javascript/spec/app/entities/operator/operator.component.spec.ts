/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { TnTestModule } from '../../../test.module';
import { OperatorComponent } from '../../../../../../main/webapp/app/entities/operator/operator.component';
import { OperatorService } from '../../../../../../main/webapp/app/entities/operator/operator.service';
import { Operator } from '../../../../../../main/webapp/app/entities/operator/operator.model';

describe('Component Tests', () => {

    describe('Operator Management Component', () => {
        let comp: OperatorComponent;
        let fixture: ComponentFixture<OperatorComponent>;
        let service: OperatorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [OperatorComponent],
                providers: [
                    OperatorService
                ]
            })
            .overrideTemplate(OperatorComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OperatorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OperatorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Operator(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.operators[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
