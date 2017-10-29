import {Component, Input, OnInit} from '@angular/core';
import {Estimation} from '../../../../estimation/estimation.model';


@Component({
    selector: 'tn-order-spreed-sheet',
    templateUrl: './order-spreed-sheet.component.html',
    styles: []
})
export class OrderSpreedSheetComponent implements OnInit {
    // settings = {
    //     data: Handsontable.helper.createSpreadsheetData(25, 50),
    //     colHeaders: true,
    //     rowHeaders: true,
    // }
    @Input() estimations: Estimation[];

    colHeaders: string[];
    columns: any[];

    constructor() {
    }

    ngOnInit() {
        this.colHeaders = ['Nazwa', 'Nr rys.', 'Ilość', 'Jedn', 'Prop. data realiz.', 'Cena', 'Data realizaciji'];
        this.columns = [
            // {
            //     data: 'id',
            //     type: 'numeric',
            //     width: 40
            // },
            // {
            //     data: 'flag',
            //     renderer: flagRenderer
            // },
            {
                data: 'itemName',
                type: 'text'
            },
            {
                data: 'itemNumber',
                type: 'text'
            },
            {
                data: 'amount',
                type: 'numeric',
                //format: '0.0000'
            },
            {
                data: 'units',
                type: 'text'
            },
            {
                data: 'nededRealizationDate',
                type: 'date',
                dateFormat: 'MM-DD-YYYY',
                correctFormat: true,
            },
            {
                data: 'estimatedCost',
                type: 'numeric',
                format: '0.00'
            },
            {
                data: 'estimatedRealizationDate',
                type: 'date',
                dateFormat: 'MM-DD-YYYY',
                correctFormat: true,
            }
        ];
    }


}
