import {Injectable} from '@angular/core';
import {Http, RequestOptions, Response, ResponseContentType,Headers} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {Attachment} from './attachment.model';
import {ResponseWrapper, createRequestOption} from '../../shared';
import * as FileSaver from 'file-saver';

@Injectable()
export class AttachmentService {

    private resourceUrl = 'api/attachments';
    private resourceSearchUrl = 'api/_search/estimations';

    constructor(private http: Http) {
    }

    // openFileForDownload(data: Response) {
    //     let blob = new Blob([data], { type: 'text/csv;charset=utf-8' });
    //     importedSaveAs(blob, 'Some.csv');
    // }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(estimation: Attachment): Attachment {
        const copy: Attachment = Object.assign({}, estimation);
        return copy;
    }

    download(attachment: Attachment): void {
        let headers = new Headers({ 'Content-Type': 'application/json'} );
        let options = new RequestOptions({responseType: ResponseContentType.Blob,headers});

        this.http.get(`${this.resourceUrl}/download/${attachment.id}`,options)
            .map((res: Response) => res.blob())
            .subscribe((data: any) => {
                this.saveDownload(data, attachment.name, attachment.dataContentType);

            });
    }

    saveDownload(responseData: any, fileName: string, contentType: string) {
        const data: Blob = new Blob([responseData], {type: contentType});

        const disableAutoBOM = true;

        FileSaver.saveAs(data, fileName, disableAutoBOM);
    }

}
