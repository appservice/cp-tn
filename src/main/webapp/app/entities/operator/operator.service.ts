import {Injectable} from '@angular/core';
import {Http, RequestOptions, Response, ResponseContentType, Headers} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';
import * as FileSaver from 'file-saver';


import {Operator} from './operator.model';
import {ResponseWrapper, createRequestOption} from '../../shared';

@Injectable()
export class OperatorService {

    private resourceUrl = SERVER_API_URL + 'api/operators';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/operators';

    constructor(private http: Http) {
    }

    create(operator: Operator): Observable<Operator> {
        const copy = this.convert(operator);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(operator: Operator): Observable<Operator> {
        const copy = this.convert(operator);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Operator> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(operator: Operator): Operator {
        const copy: Operator = Object.assign({}, operator);
        return copy;
    }

    download(operator: Operator): void {
        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({responseType: ResponseContentType.Blob, headers});

        this.http.get(`${this.resourceUrl}/${operator.id}/print-card`, options)
            .map((res: Response) => res.blob())
            .subscribe((data: any) => {
                OperatorService.saveDownload(data, operator.firstName + " " + operator.lastName, 'application/pdf');

            });
    }

    static saveDownload(responseData: any, fileName: string, contentType: string) {
        const data: Blob = new Blob([responseData], {type: contentType});

        const disableAutoBOM = true;

        FileSaver.saveAs(data, 'Karta_' + fileName + '.pdf', disableAutoBOM);
    }
}
