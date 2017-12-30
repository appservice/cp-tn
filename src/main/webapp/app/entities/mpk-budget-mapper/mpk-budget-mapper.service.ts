import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { MpkBudgetMapper } from './mpk-budget-mapper.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MpkBudgetMapperService {

    private resourceUrl =  SERVER_API_URL + 'api/mpk-budget-mappers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/mpk-budget-mappers';

    constructor(private http: Http) { }

    create(mpkBudgetMapper: MpkBudgetMapper): Observable<MpkBudgetMapper> {
        const copy = this.convert(mpkBudgetMapper);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(mpkBudgetMapper: MpkBudgetMapper): Observable<MpkBudgetMapper> {
        const copy = this.convert(mpkBudgetMapper);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<MpkBudgetMapper> {
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
     * Convert a returned JSON object to MpkBudgetMapper.
     */
    private convertItemFromServer(json: any): MpkBudgetMapper {
        const entity: MpkBudgetMapper = Object.assign(new MpkBudgetMapper(), json);
        return entity;
    }

    /**
     * Convert a MpkBudgetMapper to a JSON which can be sent to the server.
     */
    private convert(mpkBudgetMapper: MpkBudgetMapper): MpkBudgetMapper {
        const copy: MpkBudgetMapper = Object.assign({}, mpkBudgetMapper);
        return copy;
    }
}
