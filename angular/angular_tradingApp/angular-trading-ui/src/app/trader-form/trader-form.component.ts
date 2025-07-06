import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-trader-form',
  templateUrl: './trader-form.component.html',
})
export class TraderFormComponent {
  trader = {
    firstName: '',
    lastName: '',
    email: '',
    country: '',
    dateOfBirth: ''
  };

  constructor(private dialogRef: MatDialogRef<TraderFormComponent>) {}

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(form: any): void {
    if (form.valid) {
      this.dialogRef.close(this.trader);
    } else {
      form.control.markAllAsTouched();
    }
  }
}
