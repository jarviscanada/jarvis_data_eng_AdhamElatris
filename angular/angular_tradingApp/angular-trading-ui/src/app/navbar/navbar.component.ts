import { Component, OnInit } from '@angular/core';
//import { faAddressBook } from '@fortawesome/free-solid-svg-icons';
import { faAddressBook, faMoneyBillWave } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  faAddressBook = faAddressBook;
  faMoney = faMoneyBillWave;
  constructor() { }

  ngOnInit(): void {
  }

}
