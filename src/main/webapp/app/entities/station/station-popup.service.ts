import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Station } from './station.model';
import { StationService } from './station.service';

@Injectable()
export class StationPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private stationService: StationService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.stationService.find(id).subscribe((station) => {
                    if (station.validTo) {
                        station.validTo = {
                            year: station.validTo.getFullYear(),
                            month: station.validTo.getMonth() + 1,
                            day: station.validTo.getDate()
                        };
                    }
                    if (station.validFrom) {
                        station.validFrom = {
                            year: station.validFrom.getFullYear(),
                            month: station.validFrom.getMonth() + 1,
                            day: station.validFrom.getDate()
                        };
                    }
                    this.ngbModalRef = this.stationModalRef(component, station);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.stationModalRef(component, new Station());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    stationModalRef(component: Component, station: Station): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.station = station;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
