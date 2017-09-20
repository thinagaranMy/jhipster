/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { BmsappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RoutesDetailComponent } from '../../../../../../main/webapp/app/entities/routes/routes-detail.component';
import { RoutesService } from '../../../../../../main/webapp/app/entities/routes/routes.service';
import { Routes } from '../../../../../../main/webapp/app/entities/routes/routes.model';

describe('Component Tests', () => {

    describe('Routes Management Detail Component', () => {
        let comp: RoutesDetailComponent;
        let fixture: ComponentFixture<RoutesDetailComponent>;
        let service: RoutesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BmsappTestModule],
                declarations: [RoutesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RoutesService,
                    JhiEventManager
                ]
            }).overrideTemplate(RoutesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RoutesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoutesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Routes(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.routes).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
