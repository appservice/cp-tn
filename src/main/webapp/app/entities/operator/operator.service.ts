import {Injectable} from '@angular/core';
import {Http, RequestOptions, Response, ResponseContentType, Headers} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';
import * as FileSaver from 'file-saver';


import {Operator} from './operator.model';
import {ResponseWrapper, createRequestOption} from '../../shared';

@Injectable()
export class OperatorService {

    private resourceUrl =  SERVER_API_URL + 'api/operators';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/operators';

    constructor(private http: Http) {
    }

    create(operator: Operator): Observable<Operator> {
        const copy = this.convert(operator);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(operator: Operator): Observable<Operator> {
        const copy = this.convert(operator);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Operator> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
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
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Operator.
     */
    private convertItemFromServer(json: any): Operator {
        const entity: Operator = Object.assign(new Operator(), json);
        return entity;
    }

    /**
     * Convert a Operator to a JSON which can be sent to the server.
     */
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
                OperatorService.saveDownload(data, 'Karta_'+operator.firstName + " " + operator.lastName, 'application/pdf');

            });
    }

    downloadOperatorsCards(): void {
        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({responseType: ResponseContentType.Blob, headers});

        this.http.get(`${this.resourceUrl}/print-card`, options)
            .map((res: Response) => res.blob())
            .subscribe((data: any) => {
                OperatorService.saveDownload(data, "Karty operatorów", 'application/pdf');

            });
    }

    static saveDownload(responseData: any, fileName: string, contentType: string) {
        const data: Blob = new Blob([responseData], {type: contentType});

        const disableAutoBOM = true;

        FileSaver.saveAs(data,  fileName + '.pdf', disableAutoBOM);
    }
}
