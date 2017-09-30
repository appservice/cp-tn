import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Drawing } from './drawing.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DrawingService {

    private resourceUrl = 'api/drawings';
    private resourceSearchUrl = 'api/_search/drawings';

    constructor(private http: Http) { }

    create(drawing: Drawing): Observable<Drawing> {
        const copy = this.convert(drawing);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(drawing: Drawing): Observable<Drawing> {
        const copy = this.convert(drawing);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Drawing> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any,urlSearchParams?: URLSearchParams): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if(urlSearchParams){
            options.params.appendAll(urlSearchParams);
        }
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

    private convert(drawing: Drawing): Drawing {
        const copy: Drawing = Object.assign({}, drawing);
        return copy;
    }
}
