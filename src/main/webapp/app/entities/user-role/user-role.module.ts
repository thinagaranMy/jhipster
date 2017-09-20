import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BmsappSharedModule } from '../../shared';
import { BmsappAdminModule } from '../../admin/admin.module';
import {
    UserRoleService,
    UserRolePopupService,
    UserRoleComponent,
    UserRoleDetailComponent,
    UserRoleDialogComponent,
    UserRolePopupComponent,
    UserRoleDeletePopupComponent,
    UserRoleDeleteDialogComponent,
    userRoleRoute,
    userRolePopupRoute,
    UserRoleResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...userRoleRoute,
    ...userRolePopupRoute,
];

@NgModule({
    imports: [
        BmsappSharedModule,
        BmsappAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UserRoleComponent,
        UserRoleDetailComponent,
        UserRoleDialogComponent,
        UserRoleDeleteDialogComponent,
        UserRolePopupComponent,
        UserRoleDeletePopupComponent,
    ],
    entryComponents: [
        UserRoleComponent,
        UserRoleDialogComponent,
        UserRolePopupComponent,
        UserRoleDeleteDialogComponent,
        UserRoleDeletePopupComponent,
    ],
    providers: [
        UserRoleService,
        UserRolePopupService,
        UserRoleResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BmsappUserRoleModule {}
