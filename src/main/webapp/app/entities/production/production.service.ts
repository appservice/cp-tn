import {Injectable} from '@angular/core';
import {Http, Response, RequestOptions, ResponseContentType, Headers, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';

import {ResponseWrapper, createRequestOption} from '../../shared';
import {ProductionItem} from './production-item.model';



@Injectable()
export class ProductionService {

    private resourceUrl = 'api/production';
    private resourceSearchUrl = 'api/_search/orders';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }


    getItemsActualInProduction(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl+'/items-actual-in-production', options)
            .map((res: Response) => this.convertResponse(res));
    }



    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(cooperation: ProductionItem): ProductionItem {
        const copy: ProductionItem = Object.assign({}, cooperation);
        return copy;
    }




}
