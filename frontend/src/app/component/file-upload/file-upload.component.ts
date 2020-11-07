import {Component, OnInit} from '@angular/core';
import {ApiService, UserService} from '../../service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  // styleUrls: ['./api-card.component.scss']
})
export class FileUploadComponent implements OnInit {
  fileToUpload: File = null;

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

  uploadFileToActivity() {
    this.apiService.postFile(this.fileToUpload).subscribe(data => {
      // do something, if upload success
      console.log('File upload successful')
    }, error => {
      console.log(error);
    });
  }

}
