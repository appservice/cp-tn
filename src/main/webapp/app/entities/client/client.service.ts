import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Client } from './client.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ClientService {

    private resourceUrl = 'api/clients';
    private resourceSearchUrl = 'api/_search/clients';

    constructor(private http: Http) { }

    create(client: Client): Observable<Client> {
        const copy = this.convert(client);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(client: Client): Observable<Client> {
        const copy = this.convert(client);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Client> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    getAllNotPageable(req?: any): Observable<ResponseWrapper> {

        return this.http.get(this.resourceUrl+'/not-pageable')
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

    private convert(client: Client): Client {
        const copy: Client = Object.assign({}, client);
        return copy;
    }

    findAllToTypeahead(): Observable<ResponseWrapper>{
        return this.http.get(this.resourceUrl+'/to-typeahead')
            .map((res: Response) => this.convertResponse(res));
    }
}
