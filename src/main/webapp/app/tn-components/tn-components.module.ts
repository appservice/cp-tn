import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TnOkCancleAlertComponent} from './tn-ok-cancle-alert/tn-ok-cancle-alert.component';
import {TnFileUploaderComponent} from './tn-file-uploader/tn-file-uploader.component';
import {FileUploadModule} from "ng2-file-upload";
import {Autosize} from './autosize.directive';
import {PlnCurrencyPipe} from './pln-currency.pipe';
import {FileSizePipe} from './file-size-pipe';
import {TnAlert} from './tn-alert';

@NgModule({
    imports: [
        CommonModule,FileUploadModule
    ],
    declarations: [TnOkCancleAlertComponent, TnFileUploaderComponent,Autosize,PlnCurrencyPipe,FileSizePipe,TnAlert],
    exports: [TnFileUploaderComponent,Autosize,PlnCurrencyPipe, FileSizePipe,TnAlert]
})
export class TnComponentsModule {
}
