import { Component, OnInit } from '@angular/core';
import { QuotesService } from '../quotes.service';
import { Quote } from '../quote';

@Component({
  selector: 'app-quotes-list',
  templateUrl: './quotes-list.component.html',
  styleUrls: ['./quotes-list.component.css']
})
export class QuotesListComponent implements OnInit {
  quotes: Quote[] = [];
  displayedColumns: string[] = ['ticker', 'lastPrice', 'bidPrice', 'bidSize', 'askPrice', 'askSize'];

  constructor(private quotesService: QuotesService) {}

  ngOnInit(): void {
    this.quotesService.getQuotes().subscribe(data => {
      this.quotes = data;
    });
  }
}
