/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { BmsappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StationDetailComponent } from '../../../../../../main/webapp/app/entities/station/station-detail.component';
import { StationService } from '../../../../../../main/webapp/app/entities/station/station.service';
import { Station } from '../../../../../../main/webapp/app/entities/station/station.model';

describe('Component Tests', () => {

    describe('Station Management Detail Component', () => {
        let comp: StationDetailComponent;
        let fixture: ComponentFixture<StationDetailComponent>;
        let service: StationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BmsappTestModule],
                declarations: [StationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StationService,
                    JhiEventManager
                ]
            }).overrideTemplate(StationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Station(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.station).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
