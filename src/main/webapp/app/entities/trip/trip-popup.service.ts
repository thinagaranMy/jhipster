import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Trip } from './trip.model';
import { TripService } from './trip.service';

@Injectable()
export class TripPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private tripService: TripService

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
                this.tripService.find(id).subscribe((trip) => {
                    if (trip.departTime) {
                        trip.departTime = {
                            year: trip.departTime.getFullYear(),
                            month: trip.departTime.getMonth() + 1,
                            day: trip.departTime.getDate()
                        };
                    }
                    if (trip.scheduledTime) {
                        trip.scheduledTime = {
                            year: trip.scheduledTime.getFullYear(),
                            month: trip.scheduledTime.getMonth() + 1,
                            day: trip.scheduledTime.getDate()
                        };
                    }
                    if (trip.arrivalTime) {
                        trip.arrivalTime = {
                            year: trip.arrivalTime.getFullYear(),
                            month: trip.arrivalTime.getMonth() + 1,
                            day: trip.arrivalTime.getDate()
                        };
                    }
                    this.ngbModalRef = this.tripModalRef(component, trip);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tripModalRef(component, new Trip());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    tripModalRef(component: Component, trip: Trip): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.trip = trip;
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
