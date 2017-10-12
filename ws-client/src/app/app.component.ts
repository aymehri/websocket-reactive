import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  messages: Array<Message> = [];

  private ws = new WebSocket('ws://localhost:8080/echo');

  constructor() {
    this.ws.onmessage = (me: MessageEvent) => {
      const data = JSON.parse(me.data) as Message;
      console.log('test');
      this.messages.push(data);
    };
  }
}

export interface Message {
  id: string;
  message: string;
}

