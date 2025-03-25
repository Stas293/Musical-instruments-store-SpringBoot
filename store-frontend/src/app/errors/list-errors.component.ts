import { Component, Input } from "@angular/core";
import { NgForOf, NgIf } from "@angular/common";

@Component({
  selector: "app-list-errors",
  templateUrl: "./list-errors.component.html",
  imports: [NgIf, NgForOf],
  standalone: true,
})
export class ListErrorsComponent {
  errorList: string | null = null;

  @Input() set errors(error: any | null) {
    if (error) {
      this.errorList = `${error.error}`;
    } else {
      this.errorList = null;
    }
  }
}
