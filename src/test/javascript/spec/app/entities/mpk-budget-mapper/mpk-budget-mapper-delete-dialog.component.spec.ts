/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { TnTestModule } from '../../../test.module';
import { MpkBudgetMapperDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper-delete-dialog.component';
import { MpkBudgetMapperService } from '../../../../../../main/webapp/app/entities/mpk-budget-mapper/mpk-budget-mapper.service';

describe('Component Tests', () => {

    describe('MpkBudgetMapper Management Delete Component', () => {
        let comp: MpkBudgetMapperDeleteDialogComponent;
        let fixture: ComponentFixture<MpkBudgetMapperDeleteDialogComponent>;
        let service: MpkBudgetMapperService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TnTestModule],
                declarations: [MpkBudgetMapperDeleteDialogComponent],
                providers: [
                    MpkBudgetMapperService
                ]
            })
            .overrideTemplate(MpkBudgetMapperDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MpkBudgetMapperDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MpkBudgetMapperService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
