import {Component, OnInit} from '@angular/core';
import {Instrument, InstrumentPage, InstrumentService} from '../../instruments/instrument.service';
import {CurrencyPipe, LowerCasePipe, NgForOf, NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  imports: [
    LowerCasePipe,
    CurrencyPipe,
    NgForOf,
    NgOptimizedImage,
    RouterLink
  ],
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  instruments: Instrument[] = [];
  currentPage = 0;
  totalPages = 0;
  hasNext = false;
  hasPrevious = false;

  heroHeadline = 'Punny Headline';
  heroSubheading = 'And an even wittier subheading to boot.';
  heroCtaText = 'Learn More';
  heroCtaLink = '/about';

  // Features Data
  features = [
    {title: 'Feature One', description: 'Catchy description for feature one.'},
    {title: 'Feature Two', description: 'Catchy description for feature two.'},
    {title: 'Feature Three', description: 'Catchy description for feature three.'}
  ];

  constructor(private instrumentService: InstrumentService) {
  }

  ngOnInit(): void {
    this.loadInstruments(this.currentPage);
  }

  loadInstruments(page: number): void {
    this.instrumentService.getInstruments(page).subscribe((data: InstrumentPage) => {
      this.instruments = data.content;
      this.totalPages = data.totalPages;
      this.currentPage = data.number;
      this.hasNext = !data.last;
      this.hasPrevious = !data.first;
    });
  }

  nextPage(): void {
    if (this.hasNext) {
      this.loadInstruments(this.currentPage + 1);
    }
  }

  previousPage(): void {
    if (this.hasPrevious) {
      this.loadInstruments(this.currentPage - 1);
    }
  }
}
