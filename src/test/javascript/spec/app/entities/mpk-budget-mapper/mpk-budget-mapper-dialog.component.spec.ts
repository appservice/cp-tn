/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { TnTestModule } from '../../../test.module';
import { MpkBudgetMapperDialogComponent } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper-dialog.component';
import { MpkBudgetMapperService } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper.service';
import { MpkBudgetMapper } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper.model';
import { ClientService } from '../../../../../../main/webapp/app/entities/client';

describe('Component Tests', () => {

    describe('MpkBudgetMapper Management Dialog Component', () => {
        let comp: MpkBudgetMapperDialogComponent;
        let fixture: ComponentFixture<MpkBudgetMapperDialogComponent>;
        let service: MpkBudgetMapperService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [MpkBudgetMapperDialogComponent],
                providers: [
                    ClientService,
                    MpkBudgetMapperService
                ]
            })
            .overrideTemplate(MpkBudgetMapperDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MpkBudgetMapperDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MpkBudgetMapperService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MpkBudgetMapper(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.mpkBudgetMapper = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'mpkBudgetMapperListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MpkBudgetMapper();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.mpkBudgetMapper = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'mpkBudgetMapperListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
