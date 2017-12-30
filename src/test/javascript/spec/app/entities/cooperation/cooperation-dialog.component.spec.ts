/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { TnTestModule } from '../../../test.module';
import { CooperationDialogComponent } from '../../../../../../main/webapp/app/entities/cooperation/cooperation-dialog.component';
import { CooperationService } from '../../../../../../main/webapp/app/entities/cooperation/cooperation.service';
import { Cooperation } from '../../../../../../main/webapp/app/entities/cooperation/cooperation.model';
import { EstimationService } from '../../../../../../main/webapp/app/entities/estimation';

describe('Component Tests', () => {

    describe('Cooperation Management Dialog Component', () => {
        let comp: CooperationDialogComponent;
        let fixture: ComponentFixture<CooperationDialogComponent>;
        let service: CooperationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [CooperationDialogComponent],
                providers: [
                    EstimationService,
                    CooperationService
                ]
            })
            .overrideTemplate(CooperationDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CooperationDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CooperationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Cooperation(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.cooperation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cooperationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Cooperation();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.cooperation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cooperationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
