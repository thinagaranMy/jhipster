import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Routes } from './routes.model';
import { RoutesPopupService } from './routes-popup.service';
import { RoutesService } from './routes.service';
import { Station, StationService } from '../station';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-routes-dialog',
    templateUrl: './routes-dialog.component.html'
})
export class RoutesDialogComponent implements OnInit {

    routes: Routes;
    isSaving: boolean;

    stations: Station[];
    validToDp: any;
    validFromDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private routesService: RoutesService,
        private stationService: StationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.stationService.query()
            .subscribe((res: ResponseWrapper) => { this.stations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.routes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.routesService.update(this.routes));
        } else {
            this.subscribeToSaveResponse(
                this.routesService.create(this.routes));
        }
    }

    private subscribeToSaveResponse(result: Observable<Routes>) {
        result.subscribe((res: Routes) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Routes) {
        this.eventManager.broadcast({ name: 'routesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackStationById(index: number, item: Station) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-routes-popup',
    template: ''
})
export class RoutesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private routesPopupService: RoutesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.routesPopupService
                    .open(RoutesDialogComponent as Component, params['id']);
            } else {
                this.routesPopupService
                    .open(RoutesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
