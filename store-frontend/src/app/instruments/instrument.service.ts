import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface Instrument {
  id: string;
  description: string;
  title: string;
  price: number;
  quantity: number;

}

export interface InstrumentPage {
  content: Instrument[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  last: boolean;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class InstrumentService {
  private apiUrl = '/instruments';
  private defaultPageSize = 8;

  constructor(private http: HttpClient) {
  }

  getInstruments(page: number): Observable<InstrumentPage> {
    return this.http.get<InstrumentPage>(`${this.apiUrl}?page=${page}&size=${this.defaultPageSize}`);
  }
}
