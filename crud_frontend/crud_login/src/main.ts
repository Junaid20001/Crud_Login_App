import { platformBrowser } from '@angular/platform-browser';
import { AppModule } from './app/app.module';
import { CrudComponent } from './app/crud/crud.component';
import { LoginComponent } from './app/login/login.component';

platformBrowser().bootstrapModule(AppModule, {
  ngZoneEventCoalescing: true,
})
  .catch(err => console.error(err));
