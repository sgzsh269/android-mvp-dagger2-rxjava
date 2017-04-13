#### Sample app to show basics of MVP + Dagger2 + RxJava + Retrolambda. This app is based off the app - [MVP with Dagger](https://github.com/sgzsh269/android-mvp-with-dagger2).
#### Compare the Presenters ([PhotosPresenter](https://github.com/sgzsh269/android-mvp-dagger2-rxjava/compare/97bf0cfb6b64a7e9f09c85ffbe9e6fcdb9958d8b...sgzsh269:master#diff-1f5881e4225a238c9c74755b71bf6ce5), [PhotoDetailPresenter](https://github.com/sgzsh269/android-mvp-dagger2-rxjava/compare/97bf0cfb6b64a7e9f09c85ffbe9e6fcdb9958d8b...sgzsh269:master#diff-7ef7f1f0a128525dc32ac3be267dcca1)) and the Data layer ([RemoteDataSource](https://github.com/sgzsh269/android-mvp-dagger2-rxjava/compare/97bf0cfb6b64a7e9f09c85ffbe9e6fcdb9958d8b...sgzsh269:master#diff-4fa874b0dc0517944cb1c7212ad79adf), [DataRepository](https://github.com/sgzsh269/android-mvp-dagger2-rxjava/compare/97bf0cfb6b64a7e9f09c85ffbe9e6fcdb9958d8b...sgzsh269:master#diff-dcd863bfee0ec3b9dbb20dc7be7cae1e), [LocalDataSource](https://github.com/sgzsh269/android-mvp-dagger2-rxjava/compare/97bf0cfb6b64a7e9f09c85ffbe9e6fcdb9958d8b...sgzsh269:master#diff-c068dd04bd4a2c5d7c0f8c9656106c59)) between the 2 apps to see the true benefit of RxJava and Retrolambda! 

#### The sample app fetches and displays the interesting photos and their comments for the most recent day from the [Flickr API](https://www.flickr.com/services/api/).

### App Demo
![Carousel Demo](/carousel_demo.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

### License 
```
   Copyright 2017 Sagar Nilesh Shah

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```