import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Trip } from './trip.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TripService {

    private resourceUrl = 'api/trips';
    private resourceSearchUrl = 'api/_search/trips';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(trip: Trip): Observable<Trip> {
        const copy = this.convert(trip);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(trip: Trip): Observable<Trip> {
        const copy = this.convert(trip);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Trip> {
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
        entity.departTime = this.dateUtils
            .convertLocalDateFromServer(entity.departTime);
        entity.scheduledTime = this.dateUtils
            .convertLocalDateFromServer(entity.scheduledTime);
        entity.arrivalTime = this.dateUtils
            .convertLocalDateFromServer(entity.arrivalTime);
    }

    private convert(trip: Trip): Trip {
        const copy: Trip = Object.assign({}, trip);
        copy.departTime = this.dateUtils
            .convertLocalDateToServer(trip.departTime);
        copy.scheduledTime = this.dateUtils
            .convertLocalDateToServer(trip.scheduledTime);
        copy.arrivalTime = this.dateUtils
            .convertLocalDateToServer(trip.arrivalTime);
        return copy;
    }
}