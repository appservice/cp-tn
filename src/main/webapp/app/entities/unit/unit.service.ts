import { Injectable } from '@angular/core';
import {BaseRequestOptions, Http, Response, URLSearchParams} from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Unit } from './unit.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class UnitService {

    private resourceUrl = 'api/units';
    private resourceSearchUrl = 'api/_search/units';
    private resourceSearchBySentenceUrl = 'api/units-by-sentence';

    constructor(private http: Http) { }

    create(unit: Unit): Observable<Unit> {
        const copy = this.convert(unit);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(unit: Unit): Observable<Unit> {
        const copy = this.convert(unit);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Unit> {
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

    searchBySentence(sentence: string): Observable<ResponseWrapper> {
        const options: BaseRequestOptions = new BaseRequestOptions();
        const params: URLSearchParams = new URLSearchParams();
        params.set('sentence', sentence);
        options.params = params;

        // const options = createRequestOption(req);
        return this.http.get(this.resourceSearchBySentenceUrl, options)
            .map((res: any) => this.convertResponse(res).json);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(unit: Unit): Unit {
        const copy: Unit = Object.assign({}, unit);
        return copy;
    }
}
