import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StationComponent } from './station.component';
import { StationDetailComponent } from './station-detail.component';
import { StationPopupComponent } from './station-dialog.component';
import { StationDeletePopupComponent } from './station-delete-dialog.component';

@Injectable()
export class StationResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const stationRoute: Routes = [
    {
        path: 'station',
        component: StationComponent,
        resolve: {
            'pagingParams': StationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Stations'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'station/:id',
        component: StationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Stations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stationPopupRoute: Routes = [
    {
        path: 'station-new',
        component: StationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Stations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'station/:id/edit',
        component: StationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Stations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'station/:id/delete',
        component: StationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Stations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
