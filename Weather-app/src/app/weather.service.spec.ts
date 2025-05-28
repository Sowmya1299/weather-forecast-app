import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { WeatherService } from './weather.service';

describe('WeatherService', () => {
  let service: WeatherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [WeatherService],
    });

    service = TestBed.inject(WeatherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure no outstanding requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch weather data for a given city', () => {
    const mockCity = 'London';
    const mockResponse = {
      name: 'London',
      main: {
        temp: 25,
        feels_like: 24,
        temp_min: 22,
        temp_max: 27,
        pressure: 1012,
        humidity: 60,
      },
      weather: [{ main: 'Clear', description: 'clear sky', icon: '01d' }],
      wind: { speed: 5, deg: 180 },
    };

    service.getWeather(mockCity).subscribe((data) => {
      expect(data).toEqual(mockResponse);
      expect(data.name).toBe('London');
      expect(data.main.temp).toBe(25);
      expect(data.weather[0].main).toBe('Clear');
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/weather/${encodeURIComponent(mockCity)}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should handle HTTP error gracefully', () => {
    const city = 'UnknownCity';

    service.getWeather(city).subscribe({
      next: () => fail('Expected error, but got response'),
      error: (error) => {
        expect(error.status).toBe(404);
      },
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/weather/${encodeURIComponent(city)}`);
    expect(req.request.method).toBe('GET');
    req.flush('City not found', { status: 404, statusText: 'Not Found' });
  });
});