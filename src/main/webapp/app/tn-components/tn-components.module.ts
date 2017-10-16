import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TnOkCancleAlertComponent} from './tn-ok-cancle-alert/tn-ok-cancle-alert.component';
import {TnFileUploaderComponent} from './tn-file-uploader/tn-file-uploader.component';
import {FileUploadModule} from "ng2-file-upload";
import {Autosize} from './autosize.directive';
import {PlnCurrencyPipe} from './pln-currency.pipe';
import {FileSizePipe} from './file-size-pipe';
import {TnAlert} from './tn-alert';
import {TnEstimationRemarkComponent} from './tn-estimation-remark-component/tn-estimation-remark-component';
import { NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ExcelService} from './excel.service';
import {NgbdDatepickerI18n} from './datepicker-pl';

@NgModule({
    imports: [
        CommonModule, FileUploadModule,NgbModule
    ],
    declarations:
        [
            TnOkCancleAlertComponent,
            TnFileUploaderComponent,
            Autosize,
            PlnCurrencyPipe,
            FileSizePipe,
            TnAlert,
            TnEstimationRemarkComponent,
            NgbdDatepickerI18n,

        ],
    exports:
        [
            TnFileUploaderComponent,
            Autosize, PlnCurrencyPipe,
            FileSizePipe,
            TnAlert,
            TnEstimationRemarkComponent,
            NgbdDatepickerI18n


        ],
    providers:
    [
        ExcelService
    ]
})
export class TnComponentsModule {
}
