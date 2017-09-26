import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BmsappSharedModule } from '../../shared';
import { BmsappAdminModule } from '../../admin/admin.module';
import {
    TripService,
    TripPopupService,
    TripComponent,
    TripDetailComponent,
    TripDialogComponent,
    TripPopupComponent,
    TripDeletePopupComponent,
    TripDeleteDialogComponent,
    tripRoute,
    tripPopupRoute,
    TripResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...tripRoute,
    ...tripPopupRoute,
];

@NgModule({
    imports: [
        BmsappSharedModule,
        BmsappAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TripComponent,
        TripDetailComponent,
        TripDialogComponent,
        TripDeleteDialogComponent,
        TripPopupComponent,
        TripDeletePopupComponent,
    ],
    entryComponents: [
        TripComponent,
        TripDialogComponent,
        TripPopupComponent,
        TripDeleteDialogComponent,
        TripDeletePopupComponent,
    ],
    providers: [
        TripService,
        TripPopupService,
        TripResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BmsappTripModule {}
