import {Component, OnInit} from '@angular/core';
import {ApiService, UserService} from '../../service';
import {FileUploadModule} from 'primeng/fileupload';

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

  uploadFileToActivity(event, fileUpload) {
    // event.files == files to upload
    console.log(event.files)
    this.apiService.postFile(event.files[0]).subscribe(data => {
      // do something, if upload success
      console.log('File upload successful')
    }, error => {
      console.log(error);
    });
    fileUpload.clear();
  }

}
