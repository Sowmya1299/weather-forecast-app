import { Component } from '@angular/core';
import { WeatherService } from './weather.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  city: string = '';
  weatherData: any = null;
  errorMessage: string = '';
  constructor(private weatherService: WeatherService) {}
  getWeather() {
    this.errorMessage = '';
    this.weatherData = null;
    if (!this.city.trim()) {
      this.errorMessage = 'Please enter a city name.';
      return;
    }
    this.weatherService.getWeather(this.city).subscribe({
      next: (data) => {
        this.weatherData = data;
      },
      error: (err) => {
        this.errorMessage = 'Invalid Location.';
        console.error(err);
      },
    });
  }
}