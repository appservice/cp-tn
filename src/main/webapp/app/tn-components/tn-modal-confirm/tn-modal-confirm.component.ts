import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'tn-modal-confirm',
    templateUrl: './tn-modal-confirm.component.html',
    styles: []
})
export class TnModalConfirmComponent {
    @Input() contentText: string;// = 'Header';
    @Input() headerText: string;// = 'Content';

    constructor(public activeModal: NgbActiveModal) {
    }

}
