import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { WeatherService } from './weather.service';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let weatherServiceSpy: jasmine.SpyObj<WeatherService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('WeatherService', ['getWeather']);

    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [FormsModule],
      providers: [{ provide: WeatherService, useValue: spy }],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    weatherServiceSpy = TestBed.inject(WeatherService) as jasmine.SpyObj<WeatherService>;
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should show error when city is empty', () => {
    component.city = '';
    component.getWeather();
    expect(component.errorMessage).toBe('Please enter a city name.');
  });

  it('should handle error from weatherService', () => {
    weatherServiceSpy.getWeather.and.returnValue(throwError(() => new Error('404 Not Found')));

    component.city = 'InvalidCity';
    component.getWeather();

    expect(component.weatherData).toBeNull();
    expect(component.errorMessage).toBe('Invalid Location.');
  });
});
