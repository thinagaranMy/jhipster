import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TripComponent } from './trip.component';
import { TripDetailComponent } from './trip-detail.component';
import { TripPopupComponent } from './trip-dialog.component';
import { TripDeletePopupComponent } from './trip-delete-dialog.component';

@Injectable()
export class TripResolvePagingParams implements Resolve<any> {

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

export const tripRoute: Routes = [
    {
        path: 'trip',
        component: TripComponent,
        resolve: {
            'pagingParams': TripResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'trip/:id',
        component: TripDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tripPopupRoute: Routes = [
    {
        path: 'trip-new',
        component: TripPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'trip/:id/edit',
        component: TripPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'trip/:id/delete',
        component: TripDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
