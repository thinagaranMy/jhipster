import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Trip } from './trip.model';
import { TripPopupService } from './trip-popup.service';
import { TripService } from './trip.service';
import { Routes, RoutesService } from '../routes';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-trip-dialog',
    templateUrl: './trip-dialog.component.html'
})
export class TripDialogComponent implements OnInit {

    trip: Trip;
    isSaving: boolean;

    routes: Routes[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private tripService: TripService,
        private routesService: RoutesService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.routesService.query()
            .subscribe((res: ResponseWrapper) => { this.routes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.trip.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tripService.update(this.trip));
        } else {
            this.subscribeToSaveResponse(
                this.tripService.create(this.trip));
        }
    }

    private subscribeToSaveResponse(result: Observable<Trip>) {
        result.subscribe((res: Trip) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Trip) {
        this.eventManager.broadcast({ name: 'tripListModification', content: 'OK'});
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

    trackRoutesById(index: number, item: Routes) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-trip-popup',
    template: ''
})
export class TripPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tripPopupService: TripPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tripPopupService
                    .open(TripDialogComponent as Component, params['id']);
            } else {
                this.tripPopupService
                    .open(TripDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
