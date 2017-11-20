import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {Operation} from './operation.model';
import {ResponseWrapper, createRequestOption} from '../../shared';
import {OperationReportDTO} from "./operation-report.model";

@Injectable()
export class OperationService {

    private resourceUrl = 'api/operations';
    private resourceSearchUrl = 'api/_search/operations';

    constructor(private http: Http) {
    }

    create(operation: Operation): Observable<Operation> {
        const copy = this.convert(operation);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(operation: Operation): Observable<Operation> {
        const copy = this.convert(operation);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Operation> {
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

    private convert(operation: Operation): Operation {
        const copy: Operation = Object.assign({}, operation);
        return copy;
    }


    getOperationsReport(estimationId: number): Observable<ResponseWrapper> {
        return this.http.get(`${this.resourceUrl}/${estimationId}/operationsReport`).map((res: Response) => {
            console.log(res);
            return this.convertResponse(res);
        });
    }


    updateOperationReportsStatus(operationReports: OperationReportDTO []){
        // const copy = this.convert(operation);
        return this.http.put(this.resourceUrl + '/updateOperationsStatus', operationReports);
    }


    setAllOperationsFinished(estimationId: number){
        // const copy = this.convert(operation);
        return this.http.put(`${this.resourceUrl}/${estimationId}/set-all-operations-finished`, null);
    }

}
