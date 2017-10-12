package com.example.wsservice;

import java.time.Duration;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MessageWebSocketHandler implements WebSocketHandler {

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		ObjectMapper mapper = new ObjectMapper();
		
		Flux<Message> eventFlux = Flux.fromStream( Stream.generate( () -> new Message(System.currentTimeMillis(), "test")));
		Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));
		Flux<Message> resultats =  Flux.zip(eventFlux, durationFlux).map(t -> t.getT1());
		
		Publisher<WebSocketMessage> test2 = resultats
				.map(t ->  {
					try {
						return mapper.writeValueAsString(t);
					} catch (JsonProcessingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return null;
				})
				.map(t -> session.textMessage(t));
		
		/*
		Publisher<WebSocketMessage> test = Flux.generate(t ->  t.next(new Message(System.currentTimeMillis(), "test")))
				.map(t -> {
					try {
						return mapper.writeValueAsString(t);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					return null;
				})
				.map(t -> session.textMessage(t))
				.delayElements(Duration.ofSeconds(1));
				*/
		return session.send(test2);
	}

}
