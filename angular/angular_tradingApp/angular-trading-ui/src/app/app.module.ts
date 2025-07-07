import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';                
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';     
import { MatFormFieldModule } from '@angular/material/form-field'; 
import { MatDialogModule } from '@angular/material/dialog';      
import { MatButtonModule } from '@angular/material/button';    

import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NavbarComponent } from './navbar/navbar.component';
import { TraderListComponent } from './trader-list/trader-list.component';
import { TraderFormComponent } from './trader-form/trader-form.component';
import { AppRoutingModule } from './app-routing.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HttpClientModule } from '@angular/common/http';
import { TraderAccountComponent } from './trader-account/trader-account.component';
import { FundDialogComponent } from './fund-dialog/fund-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    NavbarComponent,
    TraderListComponent,
    TraderFormComponent,
    TraderAccountComponent,
    FundDialogComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,             
    BrowserAnimationsModule,
    MatTableModule,
    MatIconModule,
    MatInputModule,          
    MatFormFieldModule,   
    MatDialogModule,        
    MatButtonModule,         
    AppRoutingModule,
    FontAwesomeModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
