import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {Estimation} from '../../../estimation/estimation.model';
import {NgForm} from '@angular/forms';

@Component({
    selector: '[tn-pre-estimation]',
    templateUrl: './pre-estimation.component.html',
    styles: []
})
export class PreEstimationComponent implements OnInit {
    @Input() estimation: Estimation;
    @Input() rowIndex: number;
    @Input() editForm: NgForm;

    @Output()
    deleteRow = new EventEmitter<any>();

    constructor() {

    }

    ngOnInit() {
    }

    onDeleteRow() {
        this.deleteRow.emit(this.estimation);
    }

}
