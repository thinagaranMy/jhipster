import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Station } from './station.model';
import { StationPopupService } from './station-popup.service';
import { StationService } from './station.service';

@Component({
    selector: 'jhi-station-delete-dialog',
    templateUrl: './station-delete-dialog.component.html'
})
export class StationDeleteDialogComponent {

    station: Station;

    constructor(
        private stationService: StationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.stationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'stationListModification',
                content: 'Deleted an station'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-station-delete-popup',
    template: ''
})
export class StationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stationPopupService: StationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.stationPopupService
                .open(StationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
