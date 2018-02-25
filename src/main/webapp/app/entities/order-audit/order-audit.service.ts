import {Injectable} from '@angular/core';
import {Http, Response, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';
import {createRequestOption, ResponseWrapper} from '../../shared';


@Injectable()
export class OrderAuditService {

    private resourceUrl = 'api/audits';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }

    getAllAudits(orderId: number): Observable<ResponseWrapper> {

        return this.http.get(this.resourceUrl + '/by-orderId/'+orderId)
            .map((res: Response) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(orderAudit: any) {
        orderAudit.createdAt = this.dateUtils
            .convertDateTimeFromServer(orderAudit.createdAt);


    }



}
