import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Routes } from './routes.model';
import { RoutesPopupService } from './routes-popup.service';
import { RoutesService } from './routes.service';

@Component({
    selector: 'jhi-routes-delete-dialog',
    templateUrl: './routes-delete-dialog.component.html'
})
export class RoutesDeleteDialogComponent {

    routes: Routes;

    constructor(
        private routesService: RoutesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.routesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'routesListModification',
                content: 'Deleted an routes'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-routes-delete-popup',
    template: ''
})
export class RoutesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private routesPopupService: RoutesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.routesPopupService
                .open(RoutesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
