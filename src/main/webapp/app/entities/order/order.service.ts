import {Injectable} from '@angular/core';
import {Http, Response, RequestOptions, ResponseContentType, Headers} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';

import {Order} from './order.model';
import {ResponseWrapper, createRequestOption} from '../../shared';
import {OrderSimpleDTO} from './order-simpleDTO.model';
import * as FileSaver from 'file-saver';
import * as _ from "lodash";


@Injectable()
export class OrderService {

    private resourceUrl = 'api/orders';
    private resourceSearchUrl = 'api/_search/orders';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }

    create(order: Order): Observable<Order> {
        const copy = this.convert(order);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(order: Order): Observable<Order> {
        const copy = this.convert(order);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Order> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.closeDate = this.dateUtils
            .convertLocalDateFromServer(entity.closeDate);
        if (entity != null && entity.estimations != null) {
            for (const est of entity.estimations) {
                if (est.neededRealizationDate != null) {
                    const tempDate = this.dateUtils.convertLocalDateFromServer(est.neededRealizationDate);

                    est.neededRealizationDate = {
                        year: tempDate.getFullYear(),
                        month: tempDate.getMonth() + 1,
                        day: tempDate.getDate()
                    };

                }
                // est.neededRealizationDate
            }
        }

    }

    private convert(order: Order): Order {
        const copy: Order = _.cloneDeep(order);
        copy.closeDate = this.dateUtils
            .convertLocalDateToServer(copy.closeDate);

        if (copy.estimations != null) {

            for (const est of copy.estimations) {

                if (est.neededRealizationDate != null) {

                    est.neededRealizationDate = this.dateUtils.convertLocalDateToServer(est.neededRealizationDate);

                }
            }
        }
        return copy;
    }


    findOrderSimpleDto(id: number): Observable<OrderSimpleDTO> {
        return this.http.get(`${this.resourceUrl}/simple-dto/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            // this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }


    orderForStartEstimation(req?: any) {
        const options = createRequestOption(req);

        return this.http
            .get(`${this.resourceUrl}/not-estimated/`, options).map((res: Response) => {
                return this.convertResponse(res)
            });

    }

    ordersInEstimation(req?: any) {
        const options = createRequestOption(req);

        return this.http
            .get(`${this.resourceUrl}/in-estimation`, options).map((res: Response) => {
                return this.convertResponse(res)
            });

    }

    claimToEstimator(id: number): Observable<void> {
        return this.http.put(`${this.resourceUrl}/${id}/claim-to-estimator/`, {id: id}).map((res: Response) => {

            }
        );
    }

    // createPdfOffer(order:Order):Observable<void>{
    //     // const copy = this.convert(order);
    //
    //     return this.http.post(`${this.resourceUrl}/createPdfOffer}`,order).map((res: Response) => {
    //         return res.json();
    //     });
    // }


    createPdfOffer(order: Order): void {
        const copy = this.convert(order);

        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({responseType: ResponseContentType.Blob, headers});

        this.http.post(`${this.resourceUrl}/create-pdf-offer`, copy, options)
            .map((res: Response) => res.blob())
            .subscribe((data: any) => {
                this.saveDownload(data, order.internalNumber, 'application/pdf');

            });
    }

    saveDownload(responseData: any, fileName: string, contentType: string) {
        const data: Blob = new Blob([responseData], {type: contentType});

        const disableAutoBOM = true;

        FileSaver.saveAs(data, 'Oferta_' + fileName + '.pdf', disableAutoBOM);
    }

    moveOrderToArchive(id: number):  Observable<Response>{
       return this.http.put(`${this.resourceUrl}/${id}/move-to-archive`, null);
    }

}