import { Injectable } from '@angular/core';
import {Http, Response, URLSearchParams} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { TechnologyCard } from './technology-card.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TechnologyCardService {

    private resourceUrl = 'api/technology-cards';
    private resourceSearchUrl = 'api/_search/technology-cards';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(technologyCard: TechnologyCard): Observable<TechnologyCard> {
        const copy = this.convert(technologyCard);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(technologyCard: TechnologyCard): Observable<TechnologyCard> {
        const copy = this.convert(technologyCard);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<TechnologyCard> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any,urlSearchParams?: URLSearchParams): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if(urlSearchParams){
            options.params.appendAll(urlSearchParams);
        }
        console.log('options', options);
        return this.http.get(this.resourceUrl+"/filtered", options)
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.createdAt = this.dateUtils
            .convertDateTimeFromServer(entity.createdAt);
    }

    private convert(technologyCard: TechnologyCard): TechnologyCard {
        const copy: TechnologyCard = Object.assign({}, technologyCard);

        // copy.createdAt = this.dateUtils.toDate(technologyCard.createdAt);
        return copy;
    }
}
