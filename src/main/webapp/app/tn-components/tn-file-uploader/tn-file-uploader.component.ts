import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FileItem, FileLikeObject, FileUploader} from 'ng2-file-upload';
import {LocalStorageService, SessionStorageService} from 'ng2-webstorage';
import {Attachemnt} from '../../entities/attachment/attachment.model';
import {AttachmentService} from './tn-file-uploader.service';

// const URL = '/api/';
const URL = '/api/upload';

@Component({
    selector: 'tn-file-uploader',
    templateUrl: './tn-file-uploader.component.html',
    styleUrls: ['./tn-file-uploader.component.css'],
    providers: [AttachmentService]
})
export class TnFileUploaderComponent implements OnInit {

    @Output() notify: EventEmitter<Attachemnt[]> = new EventEmitter<Attachemnt[]>();
    @Output() onAdded: EventEmitter<Attachemnt> = new EventEmitter<Attachemnt>();
    private token: string;
    public uploader: FileUploader;
    public errorMessage: string;
    public hasBaseDropZoneOver: boolean = false;

    @Input() public attachments: Attachemnt[];
    @Input() public isViewOnly: boolean;

    constructor(private localStorage: LocalStorageService,
                private sessionStorage: SessionStorageService,
                private attachmentService: AttachmentService) {

        this.token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
        let tokenWithBearer = 'Bearer ' + this.token;

        this.uploader = new FileUploader({
            url: URL,
            authToken: tokenWithBearer,
            removeAfterUpload: true,
            autoUpload: true,
            maxFileSize: 20 * 1024 * 1024
        });
        this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
        this.onUploadItem();
    }

    ngOnInit() {
    }


    public fileOverBase(e: any): void {
        this.hasBaseDropZoneOver = e;
    }

    onDeleteItem(index: number, attachment: Attachemnt) {
        this.attachments.splice(index, 1);

        this.attachmentService.delete(attachment.id).subscribe((response) => {
            console.log(response);

        });
        this.notify.emit(this.attachments);
    }

    private onUploadItem() {
        this.errorMessage = '';
        this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {

            if (status === 200) {
                let attachment = <Attachemnt>JSON.parse(response);

                this.attachments.push(attachment);
                this.notify.emit(this.attachments);

            } else {
                console.log("Error: ", status);

            }

        }
    }

    onDownloadAttachment(attachment: Attachemnt) {
        this.attachmentService.download(attachment);
    }

    onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
        switch (filter.name) {
            case 'fileSize':
                console.log("file size to big", item, filter, options);
                this.errorMessage = `Plik ${item.name} o rozmiarze: ${(item.size / 1024 / 1024).toFixed(2)} MB jest za duzy. Dozwolona wilkość to ${options.maxFileSize / 1024 / 1024} MB`;

                break;
            case 'mimeType':
                // const allowedTypes = this.allowedMimeType.join();
                // this.errorMessage = `Type "${item.type} is not allowed. Allowed types: "${allowedTypes}"`;
                break;
            default:
            // this.errorMessage = `Unknown error (filter is ${filter.name})`;
        }
    }

}
