import { Injectable } from '@angular/core';
import { Trader } from './trader';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TraderListService {
  private traderList: Trader[] = [];

  private traderListSubject = new BehaviorSubject<Trader[]>([]);

  private dataUrl = 'assets/traders-list-data.json';  // path to your JSON file

  constructor(private http: HttpClient) {
    this.loadInitialData();
  }

  private loadInitialData(): void {
    this.http.get<Trader[]>(this.dataUrl).subscribe(data => {
      this.traderList = data;
      this.traderListSubject.next(this.traderList);
    });
  }

  getDataSource(): Observable<Trader[]> {
    return this.traderListSubject.asObservable();
  }

  getColumns(): string[] {
    return ['First Name', 'Last Name', 'Email', 'DateOfBirth', 'Country', 'Actions'];
  }

  deleteTrader(key: string): void {
    this.traderList = this.traderList.filter(trader => trader.key !== key);
    this.traderListSubject.next(this.traderList);
  }

  addTrader(trader: Trader): void {
    this.traderList.push(trader);
    this.traderListSubject.next(this.traderList);
  }
}
