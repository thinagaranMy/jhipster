import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Station } from './station.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StationService {

    private resourceUrl = 'api/stations';
    private resourceSearchUrl = 'api/_search/stations';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(station: Station): Observable<Station> {
        const copy = this.convert(station);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(station: Station): Observable<Station> {
        const copy = this.convert(station);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Station> {
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
        entity.validTo = this.dateUtils
            .convertLocalDateFromServer(entity.validTo);
        entity.validFrom = this.dateUtils
            .convertLocalDateFromServer(entity.validFrom);
    }

    private convert(station: Station): Station {
        const copy: Station = Object.assign({}, station);
        copy.validTo = this.dateUtils
            .convertLocalDateToServer(station.validTo);
        copy.validFrom = this.dateUtils
            .convertLocalDateToServer(station.validFrom);
        return copy;
    }
}
