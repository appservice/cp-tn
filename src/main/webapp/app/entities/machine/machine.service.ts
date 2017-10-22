import { Injectable } from '@angular/core';
import { Http, Response,URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Machine } from './machine.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MachineService {

    private resourceUrl = 'api/machines';
    private resourceSearchUrl = 'api/_search/machines';

    constructor(private http: Http) { }

    create(machine: Machine): Observable<Machine> {
        const copy = this.convert(machine);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(machine: Machine): Observable<Machine> {
        const copy = this.convert(machine);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Machine> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any, urlSearchParams?: URLSearchParams): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if (urlSearchParams) {
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

    private convert(machine: Machine): Machine {
        const copy: Machine = Object.assign({}, machine);
        return copy;
    }

     getMachineDtlByMachineId(machineId: number): Observable<ResponseWrapper>{
      return  this.http.get(this.resourceUrl+'/'+machineId+'/get-machine-dtls')
            .map((res: Response) => this.convertResponse(res));
    }

}
