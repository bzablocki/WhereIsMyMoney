import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ApiService, UserService} from '../../service';
import {FileUploadModule} from 'primeng/fileupload';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  // styleUrls: ['./api-card.component.scss']
})
export class FileUploadComponent implements OnInit {
  fileToUpload: File = null;

  @Output() refreshListOfTransactionsEvent: EventEmitter<any> = new EventEmitter();

  constructor(
    private apiService: ApiService
  ) {

  }

  ngOnInit(): void {
  }

  handleFileInput(files: FileList) {
    console.log(files)
    this.fileToUpload = files.item(0);
  }

  uploadFileToActivity(event, fileUpload) {
    // event.files == files to upload
    console.log(event.files)
    this.apiService.postFile(event.files[0]).subscribe(data => {
      console.log('File upload successful')
      this.refreshListOfTransactionsEvent.next(null);
    }, error => {
      console.log(error);
    });
    fileUpload.clear();
  }

}
